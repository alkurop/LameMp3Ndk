package com.omar.retromp3recorder.app.recording.recorder

import com.omar.retromp3recorder.app.common.repo.StateRepo
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.main.MainView
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.RecorderProps
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class StateLoggingVoiceRecorder @Inject internal constructor(
    @param:Named(AppComponent.DECORATOR_B) private val voiceRecorder: VoiceRecorder,
    scheduler: Scheduler,
    private val stateRepo: StateRepo
) : VoiceRecorder {
    override fun observeEvents(): Observable<VoiceRecorder.Event> {
        return voiceRecorder.observeEvents()
    }

    override fun record(props: RecorderProps) {
        stateRepo.newValue(MainView.State.Recording)
        voiceRecorder.record(props)
    }

    override fun stopRecord() {
        if (isRecording()) {
            stateRepo.newValue(MainView.State.Idle)
            voiceRecorder.stopRecord()
        }
    }

    override fun isRecording(): Boolean {
        return voiceRecorder.isRecording()
    }

    init {
        voiceRecorder.observeEvents()
            .ofType(VoiceRecorder.Event.Error::class.java)
            .map { MainView.State.Idle }
            .flatMapCompletable { state ->
                Completable.fromAction { stateRepo.newValue(state) }
            }
            .subscribeOn(scheduler)
            .subscribe()
    }
}