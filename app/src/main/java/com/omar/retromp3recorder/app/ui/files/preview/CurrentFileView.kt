package com.omar.retromp3recorder.app.ui.files.preview

import com.omar.retromp3recorder.dto.FileWrapper

object CurrentFileView {
    data class State(
        val currentFile: FileWrapper? = null,
        val isOpenFileActive: Boolean = false,
        val isDeleteFileActive: Boolean = false
    )

    sealed class Input {
        object LookForPlayableFile : Input()
    }

    sealed class Output {
        data class CurrentFileOutput(val currentFile: FileWrapper?) : CurrentFileView.Output()
        data class DeleteButtonState(val isActive: Boolean) : Output()
        data class OpenButtonState(val isActive: Boolean) : Output()
    }
}