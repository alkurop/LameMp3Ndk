package com.omar.retromp3recorder.audioplayer

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.github.alkurop.stringerbell.Stringer
import com.google.android.exoplayer2.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioPlayerExoImpl @Inject constructor(
    private val context: Context
) : AudioPlayer {
    private val events = PublishSubject.create<AudioPlayer.Event>()
    private val state = BehaviorSubject.createDefault(AudioPlayer.State.Idle)
    private val progress = BehaviorSubject.create<Pair<Long, Long>>()

    //should be nullable, because after MediaPlayer.release() becomes useless
    private var mediaPlayer: ExoPlayer = SimpleExoPlayer.Builder(context).build()
    private val handler = Handler(Looper.getMainLooper())

    override fun observeState(): Observable<AudioPlayer.State> = state

    override fun observerProgress(): Observable<Pair<Long, Long>> = progress

    override fun playerStop() {
        if (isPlaying) {
            stopMedia()
        }
    }

    override fun playerStart(options: PlayerStartOptions) {
        if (!isPlaying)
            handler.post {
                setupMediaPlayer(options.filePath, options.seekPosition)
            }
    }

    override fun observeEvents(): Observable<AudioPlayer.Event> {
        return events
    }

    override fun seek(position: Long) {
        handler.post {
            mediaPlayer.seekTo(position)
        }
    }

    private fun setupMediaPlayer(voiceURL: String, seekPosition: Long?) {
        if (!File(voiceURL).exists()) {
            events.onNext(AudioPlayer.Event.Error(Stringer(R.string.aplr_player_cannot_find_file)))
            return
        }
        mediaPlayer.apply {
            if (seekPosition != null) {
                setMediaItem(MediaItem.fromUri(voiceURL), seekPosition)
            } else {
                setMediaItem(MediaItem.fromUri(voiceURL))
            }
            playWhenReady = true
            addListener(object : Player.Listener {
                private var progressDisposable: Disposable? = null
                private val simpleExoPlayer = this@apply

                private fun sendProgressUpdate() {
                    val position = (simpleExoPlayer.currentPosition)
                    val duration = (simpleExoPlayer.duration)
                    progress.onNext(Pair(position, duration))
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        progressDisposable = Observable.interval(50, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { sendProgressUpdate() }
                        state.onNext(AudioPlayer.State.Playing)
                        events.onNext(AudioPlayer.Event.Message(Stringer(R.string.aplr_started_playing)))
                        mediaPlayer.audioComponent?.audioSessionId?.let {
                            events.onNext(AudioPlayer.Event.AudioSessionId(it))
                        }
                    } else {
                        sendProgressUpdate()
                        progressDisposable?.dispose()
                        progressDisposable = null
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
            mediaPlayer.apply {
                stop()
            }
        }
    }

    //exposing internal state instead of the mediaplayer's state
    //to have single source of truth
    override val isPlaying: Boolean
        get() = state.blockingFirst() == AudioPlayer.State.Playing
}
