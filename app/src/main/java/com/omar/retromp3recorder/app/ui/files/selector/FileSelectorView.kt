package com.omar.retromp3recorder.app.ui.files.selector

object FileSelectorView {
    data class State(
        val i: Int = 0
    )

    sealed class Input
    sealed class Output

}