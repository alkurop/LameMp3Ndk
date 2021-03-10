package com.omar.retromp3recorder.app.recording.recorder

import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder.RecorderProps
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Named

open class TestVoiceRecorder @Inject internal constructor(
    @param:Named(MockModule.RECORDER_SUBJECT) private val bus: Subject<Mp3VoiceRecorder.Event>
) : Mp3VoiceRecorder {

    private val state = BehaviorSubject.createDefault(Mp3VoiceRecorder.State.Idle)

    override fun observeEvents(): Observable<Mp3VoiceRecorder.Event> {
        return bus
    }

    override fun record(props: RecorderProps) {
        state.onNext(Mp3VoiceRecorder.State.Recording)
    }

    override fun stopRecord() {
        state.onNext(Mp3VoiceRecorder.State.Idle)
    }

    override fun isRecording(): Boolean {
        return state.blockingFirst() == Mp3VoiceRecorder.State.Recording
    }

    override fun observeState(): Observable<Mp3VoiceRecorder.State> = state
}
