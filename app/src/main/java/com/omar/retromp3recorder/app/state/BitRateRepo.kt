package com.omar.retromp3recorder.app.state

import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.recorder.RecorderDefaults
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitRateRepo @Inject constructor(defaults: RecorderDefaults) {
    private val stateSubject = BehaviorSubject.create<Mp3VoiceRecorder.BitRate>()

    init {
        stateSubject.onNext(defaults.bitRate)
    }

    fun newValue(bitRate: Mp3VoiceRecorder.BitRate) {
        stateSubject.onNext(bitRate)
    }

    fun observe(): Observable<Mp3VoiceRecorder.BitRate> {
        return stateSubject
    }
}
