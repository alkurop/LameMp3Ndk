package com.omar.retromp3recorder.app.ui.files.properties

import com.omar.retromp3recorder.dto.FileWrapper

object PropertiesView {
    data class State(
        val currentFile: FileWrapper? = null
    )

    sealed class Input
    sealed class Output {
        data class CurrentFileProperties(val currentFile: FileWrapper?) : Output()
    }
}