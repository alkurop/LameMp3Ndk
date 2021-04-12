package com.omar.retromp3recorder.utils

interface PermissionChecker {
    fun showUnchecked(permissions: Set<String>): Set<String>
}