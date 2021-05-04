package com.omar.retromp3recorder.app.ui.files.rename

object RenameFileView {
    data class State(
        val shouldDismiss: Boolean = false
    )

    sealed class Input
    sealed class Output
}