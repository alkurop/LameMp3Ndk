package com.omar.retromp3recorder.app.ui.files.preview

import com.omar.retromp3recorder.dto.FileWrapper
import com.omar.retromp3recorder.dto.Wavetable

object CurrentFileView {
    data class State(
        val filePath: String? = null,
        val wavetable: Wavetable? = null,
        val isOpenFileActive: Boolean = false,
        val isDeleteFileActive: Boolean = false,
        val isRenameButtonActive: Boolean = false,
        val playerProgress: Pair<Int, Int>? = null
    )

    sealed class Input {
        data class SeekToPosition(val position: Int) : Input()
        object SeekingStarted : Input()
    }

    sealed class Output {
        data class CurrentFileOutput(val currentFile: FileWrapper?) : CurrentFileView.Output()
        data class DeleteButtonState(val isActive: Boolean) : Output()
        data class OpenButtonState(val isActive: Boolean) : Output()
        data class RenameButtonState(val isActive: Boolean) : Output()
        data class PlayerProgress(val progress: Pair<Int, Int>?) : Output()
    }
}