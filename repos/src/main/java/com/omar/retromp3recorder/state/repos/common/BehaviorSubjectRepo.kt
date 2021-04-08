package com.omar.retromp3recorder.state.repos.common

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

open class BehaviorSubjectRepo<T : Any>(default: T? = null) {
    private val behaviorSubject: BehaviorSubject<T> =
        if (default == null) BehaviorSubject.create()
        else BehaviorSubject.createDefault(default)

    fun onNext(next: T) {
        behaviorSubject.onNext(next)
    }

    fun observe(): Observable<T> = behaviorSubject
}