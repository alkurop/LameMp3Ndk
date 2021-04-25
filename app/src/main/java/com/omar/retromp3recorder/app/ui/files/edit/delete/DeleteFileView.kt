package com.omar.retromp3recorder.app.ui.files.edit.delete

object DeleteFileView {
    data class State(
            val shouldDismiss: Boolean = false
    )

    sealed class Input {
        data class DeleteFile(val filePath: String) : Input()
    }

    sealed class Output {
        object Finished : Output()
    }
}