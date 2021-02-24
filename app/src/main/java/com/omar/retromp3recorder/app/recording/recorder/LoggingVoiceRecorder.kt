package com.omar.retromp3recorder.app.recording.recorder

import com.omar.retromp3recorder.app.common.repo.LogRepo
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.RecorderProps
import com.omar.retromp3recorder.app.utils.VarargHelper
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class LoggingVoiceRecorder @Inject internal constructor(
    @param:Named(AppComponent.DECORATOR_A) private val recorder: VoiceRecorder,
    logRepo: LogRepo,
    scheduler: Scheduler
) : VoiceRecorder {
    override fun observeEvents(): Observable<VoiceRecorder.Event> {
        return recorder.observeEvents()
    }

    override fun record(props: RecorderProps) {
        recorder.record(props)
    }

    override fun stopRecord() {
        recorder.stopRecord()
    }

    override fun isRecording(): Boolean {
        return recorder.isRecording()
    }

    init {
        val share = recorder.observeEvents().share()
        val message = share
            .ofType(VoiceRecorder.Event.Message::class.java)
            .map { answer -> LogRepo.Event.Message(answer.message) }

        val error = share
            .ofType(VoiceRecorder.Event.Error::class.java)
            .map { answer -> LogRepo.Event.Error(answer.error) }

        Observable.merge(listOf(message, error))
            .flatMapCompletable { event  ->
                Completable.fromAction { logRepo.newValue(event) }
            }
            .subscribeOn(scheduler)
            .subscribe()
    }
}
