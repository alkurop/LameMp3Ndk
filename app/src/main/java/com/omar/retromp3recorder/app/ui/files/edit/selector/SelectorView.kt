package com.omar.retromp3recorder.app.ui.files.edit.selector

object SelectorView {
    data class State(
        val items: List<Item>
    )

    sealed class Input
    sealed class Output

    data class Item(
        val fileName: String,
        val isCurrentItem: Boolean
    )
}