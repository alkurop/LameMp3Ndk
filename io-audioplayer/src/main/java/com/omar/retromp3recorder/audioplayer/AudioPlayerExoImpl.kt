package com.omar.retromp3recorder.audioplayer

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.github.alkurop.stringerbell.Stringer
import com.google.android.exoplayer2.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayerExoImpl @Inject constructor(
    private val context: Context
) : AudioPlayer {
    private val events = PublishSubject.create<AudioPlayer.Event>()
    private val state = BehaviorSubject.createDefault(AudioPlayer.State.Idle)

    //should be nullable, because after MediaPlayer.release() becomes useless
    private var mediaPlayer: ExoPlayer? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun observeState(): Observable<AudioPlayer.State> = state

    override fun playerStop() {
        if (isPlaying)
            stopMedia()
    }

    override fun playerStart(voiceURL: String) {
        if (!isPlaying)
            handler.post {
                setupMediaPlayer(voiceURL)
            }
    }

    override fun observeEvents(): Observable<AudioPlayer.Event> {
        return events
    }

    private fun setupMediaPlayer(voiceURL: String) {
        if (!File(voiceURL).exists()) {
            events.onNext(AudioPlayer.Event.Error(Stringer(R.string.aplr_player_cannot_find_file)))
            return
        }
        mediaPlayer = SimpleExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(voiceURL))
            playWhenReady = true
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if (isPlaying) {
                        state.onNext(AudioPlayer.State.Playing)
                        events.onNext(AudioPlayer.Event.Message(Stringer(R.string.aplr_started_playing)))
                        mediaPlayer?.audioComponent?.audioSessionId?.let {
                            events.onNext(AudioPlayer.Event.AudioSessionId(it))
                        }
                    } else {
                        state.onNext(AudioPlayer.State.Idle)
                        events.onNext(AudioPlayer.Event.Message(Stringer(R.string.aplr_stopped_playing)))
                    }
                }

                override fun onPlayerError(error: ExoPlaybackException) {
                    events.onNext(AudioPlayer.Event.Error(Stringer.ofString(error.toString())))
                }
            })
            prepare()
        }
    }

    private fun stopMedia() {
        handler.post {
            mediaPlayer?.apply {
                stop()
                release()
                mediaPlayer = null
            }
        }
    }

    //exposing internal state instead of the mediaplayer's state
    //to have single source of truth
    override val isPlaying: Boolean
        get() = state.blockingFirst() == AudioPlayer.State.Playing
}
