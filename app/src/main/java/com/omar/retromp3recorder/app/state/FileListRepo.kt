package com.omar.retromp3recorder.app.state

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileListRepo @Inject constructor() {
    private val stateSubject = BehaviorSubject.create<List<String>>()

    fun newValue(fileNames: List<String>) {
        stateSubject.onNext(fileNames)
    }

    fun observe(): Observable<List<String>> {
        return stateSubject
    }
}