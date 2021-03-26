package com.omar.retromp3recorder.app.ui.main

import com.github.alkurop.ghostinshell.Shell

object MainView {
    data class State(
        val requestForPermissions: Shell<Set<String>>,
    )
    sealed class Input
    sealed class Output {
        data class RequestPermissionsOutput(val permissionsToRequest: Set<String>) : Output()
    }
}