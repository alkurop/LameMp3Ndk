package com.omar.retromp3recorder.state.repos

import com.github.alkurop.ghostinshell.Shell
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestPermissionsRepo @Inject constructor() {
    private val state: BehaviorSubject<ShouldRequestPermissions> = BehaviorSubject.create()
    fun observe(): Observable<ShouldRequestPermissions> {
        return state
    }

    fun onNext(newValue: ShouldRequestPermissions) {
        state.onNext(newValue)
    }

    sealed class ShouldRequestPermissions {
        object Granted : ShouldRequestPermissions()
        data class Denied(val permissions: Shell<Set<String>>) : ShouldRequestPermissions()
    }
}