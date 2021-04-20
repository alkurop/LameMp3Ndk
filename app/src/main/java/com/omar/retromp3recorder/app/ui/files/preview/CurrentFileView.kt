package com.omar.retromp3recorder.app.ui.files.preview

import com.github.alkurop.stringerbell.Stringer

object CurrentFileView {
    data class State(
        val currentFileName: Stringer,
        val isShowingFileButtons: Boolean
    )

    sealed class Input {
        object LookForPlayableFile : Input()
    }

    sealed class Output {
        data class CurrentFileOutput(val currentFileName: String?) : CurrentFileView.Output()
        object AudioActive : Output()
        object AudioInactive : Output()
    }
}