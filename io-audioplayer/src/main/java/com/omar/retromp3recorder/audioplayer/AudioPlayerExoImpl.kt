package com.omar.retromp3recorder.audioplayer

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.github.alkurop.stringerbell.Stringer
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.Player.STATE_ENDED
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
    context: Context
) : AudioPlayer {
    private val events = PublishSubject.create<AudioPlayer.Output.Event>()
    private val state = BehaviorSubject.createDefault(AudioPlayer.State.Idle)
    private val progress = BehaviorSubject.create<AudioPlayer.Output.Progress>()
    private val mediaPlayer: ExoPlayer = SimpleExoPlayer.Builder(context).build()
    private val handler = Handler(Looper.getMainLooper())

    override fun observe(): Observable<AudioPlayer.Output> =
        Observable.merge(
            progress,
            events
        )

    override fun observeState(): Observable<AudioPlayer.State> = state

    override fun onInput(input: AudioPlayer.Input) {
        when (input) {
            is AudioPlayer.Input.Pause -> {
                mediaPlayer.pause()
                state.onNext(AudioPlayer.State.Paused)
                events.onNext(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_pause)))
            }
            is AudioPlayer.Input.Resume -> {
                mediaPlayer.play()
                events.onNext(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_resume)))
                state.onNext(AudioPlayer.State.Playing)
            }
            is AudioPlayer.Input.SeekPause -> {
                mediaPlayer.pause()
                events.onNext(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_seek_pause)))
                state.onNext(AudioPlayer.State.Seek_Paused)
            }
            is AudioPlayer.Input.Seek -> {
                handler.post {
                    events.onNext(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_seek)))
                    mediaPlayer.seekTo(input.position)
                }
            }
            is AudioPlayer.Input.Stop -> {
                if (isPlaying) {
                    stopMedia()
                }
            }
            is AudioPlayer.Input.Start -> {
                if (!isPlaying)
                    handler.post {
                        setupMediaPlayer(input.options.filePath, input.options.seekPosition)
                    }
            }
        }
    }

    private fun setupMediaPlayer(voiceURL: String, seekPosition: Long?) {
        if (!File(voiceURL).exists()) {
            events.onNext(AudioPlayer.Output.Event.Error(Stringer(R.string.aplr_player_cannot_find_file)))
            return
        }
        mediaPlayer.apply {
            if (seekPosition != null) {
                setMediaItem(MediaItem.fromUri(voiceURL), seekPosition)
            } else {
                setMediaItem(MediaItem.fromUri(voiceURL))
            }
            playWhenReady = true
            state.onNext(AudioPlayer.State.Playing)
            addListener(object : Player.Listener {
                private var progressDisposable: Disposable? = null
                private val simpleExoPlayer = this@apply

                private fun sendProgressUpdate() {
                    val position = (simpleExoPlayer.currentPosition)
                    val duration = (simpleExoPlayer.duration)
                    progress.onNext(AudioPlayer.Output.Progress(position, duration))
                }

                override fun onPlaybackStateChanged(state: Int) {
                    if (state == STATE_ENDED) {
                        stopMedia()
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    if (isPlaying) {
                        progressDisposable = Observable.interval(50, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { sendProgressUpdate() }
                        mediaPlayer.audioComponent?.audioSessionId?.let {
                            events.onNext(AudioPlayer.Output.Event.AudioSessionId(it))
                        }
                        events.onNext(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_started_playing)))
                    } else {
                        sendProgressUpdate()
                        progressDisposable?.dispose()
                        progressDisposable = null
                    }
                }

                override fun onPlayerError(error: ExoPlaybackException) {
                    events.onNext(AudioPlayer.Output.Event.Error(Stringer.ofString(error.toString())))
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
            state.onNext(AudioPlayer.State.Idle)
            events.onNext(AudioPlayer.Output.Event.Message(Stringer(R.string.aplr_stopped_playing)))
        }
    }
}
