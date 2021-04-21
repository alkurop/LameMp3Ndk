package com.omar.retromp3recorder.app.ui.files.edit.delete

object DeleteFileView {
    data class State(
        val shouldDismiss: Boolean = false
    )

    sealed class Input
    sealed class Output
}