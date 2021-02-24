package com.omar.retromp3recorder.app.common.repo

import com.omar.retromp3recorder.app.main.MainView
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StateRepo @Inject constructor() {
    private val stateSubject = BehaviorSubject
        .createDefault(MainView.State.Idle)

    fun newValue(state: MainView.State) {
        stateSubject.onNext(state)
    }

    fun observe(): Observable<MainView.State> {
        return stateSubject
    }
}