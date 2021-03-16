package com.omar.retromp3recorder.app.state

import com.omar.retromp3recorder.app.modules.recording.Mp3VoiceRecorder.SampleRate
import com.omar.retromp3recorder.app.modules.recording.RecorderDefaults
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleRateRepo @Inject constructor(defaults: RecorderDefaults) {
    private val stateSubject = BehaviorSubject.create<SampleRate>()

    init {
        stateSubject.onNext(defaults.sampleRate)
    }

    fun newValue(bitRate: SampleRate) {
        stateSubject.onNext(bitRate)
    }

    fun observe(): Observable<SampleRate> {
        return stateSubject
    }
}
