package com.omar.retromp3recorder.app.ui.files.preview

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.FileWrapper
import com.omar.retromp3recorder.dto.PlayerProgress
import com.omar.retromp3recorder.dto.Wavetable
import com.omar.retromp3recorder.utils.Optional

object CurrentFileView {
    data class State(
        val filePath: String? = null,
        val wavetable: Shell<Wavetable> = Shell.empty(),
        val isOpenFileActive: Boolean = false,
        val isDeleteFileActive: Boolean = false,
        val isRenameButtonActive: Boolean = false,
        val isRecording: Boolean = false,
        val playerProgress: Shell<PlayerProgress> = Shell.empty()
    )

    sealed class Input {
        data class SeekToPosition(val position: Int) : Input()
        object SeekingStarted : Input()
        object SeekingFinished : Input()
    }

    sealed class Output {
        data class CurrentFileOutput(val currentFile: FileWrapper?) : CurrentFileView.Output()
        data class DeleteButtonState(val isActive: Boolean) : Output()
        data class OpenButtonState(val isActive: Boolean) : Output()
        data class RenameButtonState(val isActive: Boolean) : Output()
        data class PlayerProgressOut(val progress: Optional<PlayerProgress>) : Output()
        data class IsRecordingOut(val isRecording: Boolean) : Output()
    }
}