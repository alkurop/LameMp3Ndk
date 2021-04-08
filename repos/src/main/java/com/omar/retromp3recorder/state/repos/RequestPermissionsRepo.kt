package com.omar.retromp3recorder.state.repos

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.state.repos.common.BehaviorSubjectRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestPermissionsRepo @Inject constructor() : BehaviorSubjectRepo<RequestPermissionsRepo.ShouldRequestPermissions>(){
    sealed class ShouldRequestPermissions {
        object Granted : ShouldRequestPermissions()
        data class Denied(val permissions: Shell<Set<String>>) : ShouldRequestPermissions()
    }
}