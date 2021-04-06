package com.omar.retromp3recorder.state.repos

import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.recorder.RecorderDefaults
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleRateRepo @Inject constructor(
    defaults: RecorderDefaults
) {
    private val stateSubject = BehaviorSubject.create<Mp3VoiceRecorder.SampleRate>()

    init {
        stateSubject.onNext(defaults.sampleRate)
    }

    fun onNext(bitRate: Mp3VoiceRecorder.SampleRate) {
        stateSubject.onNext(bitRate)
    }

    fun observe(): Observable<Mp3VoiceRecorder.SampleRate> {
        return stateSubject
    }
}
