package com.omar.retromp3recorder.app.playback.player

import android.media.MediaPlayer
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.utils.NotUnitTestable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.io.File
import java.io.IOException
import javax.inject.Inject

@NotUnitTestable
class AudioPlayerRx @Inject constructor() :
    AudioPlayer,
    StatefulAudioPlayer {

    private val events = PublishSubject.create<AudioPlayer.Event>()
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private val state = BehaviorSubject.createDefault(StatefulAudioPlayer.State.Idle)

    override fun observeState(): Observable<StatefulAudioPlayer.State> =
        state.distinctUntilChanged()

    override fun playerStop() {
        stopMedia()
    }

    override fun playerStart(voiceURL: String) {
        setupMediaPlayer(voiceURL)
    }

    override fun observeEvents(): Observable<AudioPlayer.Event> {
        return events
    }

    private fun setupMediaPlayer(voiceURL: String) {
        if (!File(voiceURL).exists()) {
            events.onNext(AudioPlayer.Event.Error(Stringer(R.string.player_cannot_find_file)))
            return
        }
        mediaPlayer.apply {
            setOnCompletionListener { stopMedia() }
            setOnPreparedListener { playMedia() }
            try {
                setDataSource(voiceURL)
                prepareAsync()
            } catch (e: IOException) {
                events.onNext(AudioPlayer.Event.Error(Stringer(R.string.not_recorder_yet)))
            }
        }

    }

    private fun stopMedia() {
        mediaPlayer
            .takeIf { it.isPlaying }
            ?.let {
                state.onNext(StatefulAudioPlayer.State.Idle)
                it.stop()
                it.release()
                events.onNext(AudioPlayer.Event.Message(Stringer(R.string.stopped_playing)))
                events.onNext(AudioPlayer.Event.PlaybackEnded)
            }
    }

    private fun playMedia() {
        val mediaPlayer = mediaPlayer
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            state.onNext(StatefulAudioPlayer.State.Playing)
            events.onNext(AudioPlayer.Event.SendPlayerId(mediaPlayer.audioSessionId))
            events.onNext(AudioPlayer.Event.Message(Stringer(R.string.started_playing)))
        } else {
            events.onNext(AudioPlayer.Event.Error(Stringer(R.string.player_error_on_stop)))
        }
    }

    //exposing internal state instead of the mediaplayer's state
    //to have single source of truth
    override val isPlaying: Boolean
        get() = state.blockingFirst() == StatefulAudioPlayer.State.Playing

}
