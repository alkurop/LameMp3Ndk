package com.omar.retromp3recorder.app.recording.recorder

import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.RecorderProps
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Named

open class TestVoiceRecorder @Inject internal constructor(
    @param:Named(MockModule.RECORDER_SUBJECT) private val bus: Subject<VoiceRecorder.Event>
) : VoiceRecorder, StatefulVoiceRecorder {

    private val state = BehaviorSubject.createDefault(StatefulVoiceRecorder.State.Idle)

    override fun observeEvents(): Observable<VoiceRecorder.Event> {
        return bus
    }

    override fun record(props: RecorderProps) {
        state.onNext(StatefulVoiceRecorder.State.Recording)
    }

    override fun stopRecord() {
        state.onNext(StatefulVoiceRecorder.State.Idle)
    }

    override fun isRecording(): Boolean {
        return state.blockingFirst() == StatefulVoiceRecorder.State.Recording
    }

    override fun observeState(): Observable<StatefulVoiceRecorder.State> = state
}
