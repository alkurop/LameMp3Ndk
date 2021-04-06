package com.omar.retromp3recorder.state.repos

import com.omar.retromp3recorder.recorder.RecorderDefaults
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentFileRepo @Inject constructor(defaults: RecorderDefaults) {
    private val stateSubject = BehaviorSubject.create<String>()

    fun onNext(bitRate: String) {
        stateSubject.onNext(bitRate)
    }

    fun observe(): Observable<String> {
        return stateSubject
    }

    init {
        stateSubject.onNext(defaults.filePath)
    }
}