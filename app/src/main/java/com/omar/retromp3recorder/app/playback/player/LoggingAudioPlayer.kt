package com.omar.retromp3recorder.app.playback.player

import com.omar.retromp3recorder.app.common.repo.LogRepo
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.utils.VarargHelper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class LoggingAudioPlayer @Inject internal constructor(
    @param:Named(AppComponent.DECORATOR_A) private val audioPlayer: AudioPlayer,
    scheduler: Scheduler,
    logRepo: LogRepo
) : AudioPlayer {
    override fun playerStop() {
        if (audioPlayer.isPlaying) {
            audioPlayer.playerStop()
        }
    }

    override fun playerStart(voiceURL: String) {
        audioPlayer.playerStart(voiceURL)
    }

    override fun observeEvents(): Observable<AudioPlayer.Event> {
        return audioPlayer.observeEvents()
    }

    override val isPlaying: Boolean
        get() = audioPlayer.isPlaying

    init {
        val share = audioPlayer.observeEvents().share()
        val message: Observable<LogRepo.Event> = share
            .ofType(AudioPlayer.Event.Message::class.java)
            .map { answer -> LogRepo.Event.Message(answer.message) }

        val error: Observable<LogRepo.Event> = share
            .ofType(AudioPlayer.Event.Error::class.java)
            .map { answer -> LogRepo.Event.Error(answer.error) }

        Observable
            .merge(VarargHelper.createLinkedList(message, error))
            .flatMapCompletable { event ->
                Completable.fromAction { logRepo.newValue(event) }
            }
            .subscribeOn(scheduler)
            .subscribe()
    }
}
