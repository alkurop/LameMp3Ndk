package com.omar.retromp3recorder.bl

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo.ShouldRequestPermissions.Denied
import com.omar.retromp3recorder.utils.PermissionChecker
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CheckPermissionsUC @Inject constructor(
    private val permissionChecker: PermissionChecker,
    private val requestPermissionsRepo: RequestPermissionsRepo
) {
    fun execute(permissions: Set<String>): Completable {
        return Observable
            .fromCallable {
                val uncheckedPermissions = permissionChecker.showUnchecked(permissions)
                if (uncheckedPermissions.isEmpty()) RequestPermissionsRepo.ShouldRequestPermissions.Granted else Denied(
                    Shell(uncheckedPermissions)
                )
            }
            .flatMapCompletable { shouldRequestPermissions ->
                Completable.fromAction { requestPermissionsRepo.onNext(shouldRequestPermissions) }
            }
    }
}
