package com.omar.retromp3recorder.state.repos

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileListRepo @Inject constructor() {
    private val stateSubject = BehaviorSubject.create<List<String>>()

    fun onNext(fileNames: List<String>) {
        stateSubject.onNext(fileNames)
    }

    fun observe(): Observable<List<String>> {
        return stateSubject
    }
}