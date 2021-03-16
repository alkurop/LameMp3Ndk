package com.omar.retromp3recorder.audioplayer

import android.media.MediaPlayer
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.utils.NotUnitTestable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.io.File
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@NotUnitTestable
class AudioPlayerImpl @Inject constructor() : AudioPlayer {

    private val events = PublishSubject.create<AudioPlayer.Event>()
    private val state = BehaviorSubject.createDefault(AudioPlayer.State.Idle)

    //should be nullable, because after MediaPlayer.release() becomes useless
    private var mediaPlayer: MediaPlayer? = null

    override fun observeState(): Observable<AudioPlayer.State> = state

    override fun playerStop() {
        if (isPlaying)
            stopMedia()
    }

    override fun playerStart(voiceURL: String) {
        if (!isPlaying)
            setupMediaPlayer(voiceURL)
    }

    override fun observeEvents(): Observable<AudioPlayer.Event> {
        return events
    }

    private fun setupMediaPlayer(voiceURL: String) {
        if (!File(voiceURL).exists()) {
            events.onNext(AudioPlayer.Event.Error(Stringer(R.string.aplr_player_cannot_find_file)))
            return
        }
        mediaPlayer = MediaPlayer().apply {
            setOnCompletionListener { stopMedia() }
            setOnPreparedListener { playMedia() }
            try {
                setDataSource(voiceURL)
                prepareAsync()
            } catch (e: IOException) {
                events.onNext(AudioPlayer.Event.Error(Stringer(R.string.aplr_not_recorder_yet)))
            } catch (e: Exception) {
                events.onNext(AudioPlayer.Event.Error(Stringer(R.string.something_went_wrong)))
                Timber.e(e)
            }
        }
    }

    private fun stopMedia() {
        mediaPlayer
            ?.let {
                state.onNext(AudioPlayer.State.Idle)
                it.stop()
                it.release()
                mediaPlayer = null
                events.onNext(AudioPlayer.Event.Message(Stringer(R.string.aplr_stopped_playing)))
            }
    }

    private fun playMedia() {
        val mediaPlayer = mediaPlayer ?: return
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            state.onNext(AudioPlayer.State.Playing)
            events.onNext(AudioPlayer.Event.PlayerId(mediaPlayer.audioSessionId))
            events.onNext(AudioPlayer.Event.Message(Stringer(R.string.aplr_started_playing)))
        } else {
            events.onNext(AudioPlayer.Event.Error(Stringer(R.string.aplr_player_error_on_stop)))
        }
    }

    //exposing internal state instead of the mediaplayer's state
    //to have single source of truth
    override val isPlaying: Boolean
        get() = state.blockingFirst() == AudioPlayer.State.Playing

}
