package com.omar.retromp3recorder.app.recording.usecase

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo.ShouldRequestPermissions.Denied
import com.omar.retromp3recorder.app.utils.NotUnitTestable
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

interface CheckPermissionsUC {
    fun execute(permissions: Set<String>): Completable
}

@NotUnitTestable
class CheckPermissionsUCImpl @Inject constructor(
    private val context: Context,
    private val requestPermissionsRepo: RequestPermissionsRepo
) : CheckPermissionsUC {
    override fun execute(permissions: Set<String>): Completable {
        return Observable
            .fromCallable {
                val requestPermissions =
                    permissions.mapNotNull { permission ->
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            permission
                        ) == PackageManager.PERMISSION_GRANTED
                        if (hasPermission) null else permission
                    }.toSet()
                if (requestPermissions.isEmpty()) RequestPermissionsRepo.ShouldRequestPermissions.Granted else Denied(
                    Shell(requestPermissions)
                )
            }
            .flatMapCompletable { shouldRequestPermissions ->
                Completable.fromAction { requestPermissionsRepo.newValue(shouldRequestPermissions) }
            }
    }

}
