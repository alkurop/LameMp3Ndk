package com.omar.retromp3recorder.app.recording.repo

import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder.BitRate
import com.omar.retromp3recorder.app.recording.recorder.RecorderDefaults
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BitRateRepo @Inject constructor(defaults: RecorderDefaults) {
    private val stateSubject = BehaviorSubject.create<BitRate>()

    init {
        stateSubject.onNext(defaults.bitRate)
    }

    fun newValue(bitRate: BitRate) {
        stateSubject.onNext(bitRate)
    }

    fun observe(): Observable<BitRate> {
        return stateSubject
    }
}
