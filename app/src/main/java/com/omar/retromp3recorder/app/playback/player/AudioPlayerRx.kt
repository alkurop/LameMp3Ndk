package com.omar.retromp3recorder.app.playback.player

import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.utils.NotUnitTestable
import com.omar.retromp3recorder.app.utils.Stringer
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.File
import java.io.IOException
import javax.inject.Inject

@NotUnitTestable
class AudioPlayerRx @Inject constructor(private val stringer: Stringer) :
    OnCompletionListener,
    OnPreparedListener, AudioPlayer {
    private val events = PublishSubject.create<AudioPlayer.Event>()
    private var mediaPlayer: MediaPlayer? = null

    override fun playerStop() {
        stopMedia()
    }

    override fun playerStart(voiceURL: String) {
        setupMediaPlayer(voiceURL)
    }

    override fun observeEvents(): Observable<AudioPlayer.Event> {
        return events
    }

    override fun onCompletion(mediaPlayer: MediaPlayer) {
        stopMedia()
    }

    override fun onPrepared(mediaPlayer: MediaPlayer) {
        playMedia()
    }

    private fun setupMediaPlayer(voiceURL: String) {
        if (!File(voiceURL).exists()) {
            events.onNext(AudioPlayer.Event.Error(stringer.getString(R.string.player_cannot_find_file)))
            return
        }
        mediaPlayer = MediaPlayer().apply {
            setOnCompletionListener(this@AudioPlayerRx)
            setOnPreparedListener(this@AudioPlayerRx)
            try {
                setDataSource(voiceURL)
                prepareAsync()
            } catch (e: IOException) {
                events.onNext(AudioPlayer.Event.Error(stringer.getString(R.string.not_recorder_yet)))
            }
        }

    }

    private fun stopMedia() {
        mediaPlayer?.let {
            it.stop()
            it.release()
            mediaPlayer = null
            events.onNext(AudioPlayer.Event.Message(stringer.getString(R.string.stopped_playing)))
            events.onNext(AudioPlayer.Event.PlaybackEnded)
        }
    }

    private fun playMedia() {
        val mediaPlayer = mediaPlayer
        if (mediaPlayer != null && !mediaPlayer.isPlaying) {
            mediaPlayer.start()
            events.onNext(AudioPlayer.Event.SendPlayerId(mediaPlayer.audioSessionId))
            events.onNext(AudioPlayer.Event.Message(stringer.getString(R.string.started_playing)))
        } else {
            events.onNext(AudioPlayer.Event.Error(stringer.getString(R.string.player_error_on_stop)))
        }
    }

    override val isPlaying: Boolean
        get() = mediaPlayer != null && mediaPlayer?.isPlaying == true

}
