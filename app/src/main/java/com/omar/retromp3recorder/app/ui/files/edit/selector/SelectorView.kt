package com.omar.retromp3recorder.app.ui.files.edit.selector

object SelectorView {
    data class State(
        val i: Int = 0
    )

    sealed class Input
    sealed class Output
}