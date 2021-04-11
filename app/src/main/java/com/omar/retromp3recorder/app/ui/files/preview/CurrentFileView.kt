package com.omar.retromp3recorder.app.ui.files.preview

import java.io.File

object CurrentFileView {
    data class State(
        val currentName: String?
    )

    sealed class Input
    sealed class Output {
        data class CurrentFileOutput(val currentFile: File?) : CurrentFileView.Output()
    }
}