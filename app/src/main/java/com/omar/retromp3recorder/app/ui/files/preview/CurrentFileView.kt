package com.omar.retromp3recorder.app.ui.files.preview

object CurrentFileView {
    data class State(
        val currentFilePath: String? = null,
        val isOpenFileActive: Boolean = false,
        val isDeleteFileActive: Boolean = false
    )

    sealed class Input {
        object LookForPlayableFile : Input()
    }

    sealed class Output {
        data class CurrentFileOutput(val currentFilePath: String?) : CurrentFileView.Output()
        data class DeleteButtonState(val isActive: Boolean) : Output()
        data class OpenButtonState(val isActive: Boolean) : Output()
    }
}