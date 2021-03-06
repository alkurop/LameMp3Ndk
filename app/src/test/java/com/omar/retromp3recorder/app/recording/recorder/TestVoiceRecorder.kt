package com.omar.retromp3recorder.app.recording.recorder

import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.RecorderProps
import io.reactivex.Observable
import io.reactivex.subjects.Subject
import javax.inject.Inject
import javax.inject.Named

class TestVoiceRecorder @Inject internal constructor(
    @param:Named(MockModule.RECORDER_SUBJECT) private val bus: Subject<VoiceRecorder.Event>
) :
    VoiceRecorder {
    override fun observeEvents(): Observable<VoiceRecorder.Event> {
        return bus
    }

    override fun record(props: RecorderProps) {}
    override fun stopRecord() {}
    override fun isRecording(): Boolean {
        return true
    }
}
