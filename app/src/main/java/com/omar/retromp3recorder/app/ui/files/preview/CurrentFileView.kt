package com.omar.retromp3recorder.app.ui.files.preview

import com.github.alkurop.stringerbell.Stringer
import java.io.File

object CurrentFileView {
    data class State(
        val currentFileName: Stringer
    )

    sealed class Input
    sealed class Output {
        data class CurrentFileOutput(val currentFile: File?) : CurrentFileView.Output()
    }
}