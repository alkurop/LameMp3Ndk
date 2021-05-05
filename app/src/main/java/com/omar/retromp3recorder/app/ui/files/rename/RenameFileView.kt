package com.omar.retromp3recorder.app.ui.files.rename

object RenameFileView {
    data class State(
        val shouldDismiss: Boolean = false,
        val isOkButtonEnabled: Boolean = false
    )

    sealed class Input {
        data class CheckCanRename(
            val filePath: String,
            val newName: String
        ) : Input()

        data class Rename(
            val filePath: String,
            val newName: String
        ) : Input()
    }

    sealed class Output {
        object Dismiss : Output()
        data class OkButtonState(val isEnabled: Boolean) : Output()
    }
}