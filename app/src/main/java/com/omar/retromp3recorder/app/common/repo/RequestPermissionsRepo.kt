package com.omar.retromp3recorder.app.common.repo

import com.omar.retromp3recorder.app.utils.OneShot
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestPermissionsRepo @Inject constructor() {
    private val state: BehaviorSubject<OneShot<ShouldRequestPermissions>> = BehaviorSubject.create()
    fun observe(): Observable<OneShot<ShouldRequestPermissions>> {
        return state
    }

    fun newValue(newValue: ShouldRequestPermissions) {
        state.onNext(OneShot(newValue))
    }

    sealed class ShouldRequestPermissions {
        object Granted : ShouldRequestPermissions()
        data class Denied(val permissions: Set<String>) : ShouldRequestPermissions()
    }
}