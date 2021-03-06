package com.omar.retromp3recorder.app.files.repo

import com.omar.retromp3recorder.app.recording.recorder.RecorderDefaults
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentFileRepo @Inject constructor(defaults: RecorderDefaults) {
    private val stateSubject = BehaviorSubject.create<String>()
    fun newValue(bitRate: String) {
        stateSubject.onNext(bitRate)
    }

    fun observe(): Observable<String> {
        return stateSubject
    }

    fun hasValue(): Boolean {
        return stateSubject.hasValue()
    }

    init {
        stateSubject.onNext(defaults.filePath)
    }
}