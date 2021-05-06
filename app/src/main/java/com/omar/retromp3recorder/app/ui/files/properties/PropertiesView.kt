package com.omar.retromp3recorder.app.ui.files.properties

import com.omar.retromp3recorder.dto.ExistingFileWrapper

object PropertiesView {
    data class State(
        val currentFile: ExistingFileWrapper? = null
    )

    sealed class Input
    sealed class Output {
        data class CurrentFileProperties(val currentFile: ExistingFileWrapper?) : Output()
    }
}