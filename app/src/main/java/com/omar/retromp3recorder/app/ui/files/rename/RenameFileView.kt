package com.omar.retromp3recorder.app.ui.files.rename

import com.omar.retromp3recorder.dto.ExistingFileWrapper

object RenameFileView {
    data class State(
        val shouldDismiss: Boolean = false,
        val isOkButtonEnabled: Boolean = false,
        val fileWrapper: ExistingFileWrapper? = null
    )

    sealed class Input {
        data class CheckCanRename(
            val newName: String
        ) : Input()

        data class Rename(
            val newName: String
        ) : Input()
    }

    sealed class Output {
        data class CanDismiss(val isDismissed: Boolean) : Output()
        data class OkButtonState(val isEnabled: Boolean) : Output()
        data class CurrentFile(val fileWrapper: ExistingFileWrapper) : Output()
    }
}