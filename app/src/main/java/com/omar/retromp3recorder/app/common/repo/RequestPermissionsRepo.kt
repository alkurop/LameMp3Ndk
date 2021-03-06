package com.omar.retromp3recorder.app.common.repo

import com.github.alkurop.ghostinshell.Shell
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestPermissionsRepo @Inject constructor() {
    private val state: BehaviorSubject<Shell<ShouldRequestPermissions>> = BehaviorSubject.create()
    fun observe(): Observable<Shell<ShouldRequestPermissions>> {
        return state
    }

    fun newValue(newValue: ShouldRequestPermissions) {
        state.onNext(Shell(newValue))
    }

    sealed class ShouldRequestPermissions {
        object Granted : ShouldRequestPermissions()
        data class Denied(val permissions: Set<String>) : ShouldRequestPermissions()
    }
}