package com.omar.retromp3recorder.app.ui.files.preview

object CurrentFileView {
    data class State(
        val currentFilePath: String?,
        val isShowingFileButtons: Boolean
    )

    sealed class Input {
        object LookForPlayableFile : Input()
    }

    sealed class Output {
        data class CurrentFileOutput(val currentFilePath: String?) : CurrentFileView.Output()
        object AudioActive : Output()
        object AudioInactive : Output()
    }
}