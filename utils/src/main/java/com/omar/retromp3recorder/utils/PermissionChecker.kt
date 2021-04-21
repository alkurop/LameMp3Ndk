package com.omar.retromp3recorder.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import javax.inject.Inject

interface PermissionChecker {
    fun showUnchecked(permissions: Set<String>): Set<String>
}

class PermissionCheckerImpl @Inject constructor(
    private val context: Context,
) : PermissionChecker {
    override fun showUnchecked(permissions: Set<String>): Set<String> {
        return permissions.mapNotNull { permission ->
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
            if (hasPermission) null else permission
        }.toSet()
    }
}