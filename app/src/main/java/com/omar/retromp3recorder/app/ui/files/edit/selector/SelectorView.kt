package com.omar.retromp3recorder.app.ui.files.edit.selector

object SelectorView {
    data class State(
        val items: List<Item>
    )

    sealed class Input {
        data class ItemSelected(val item: Item) : Input()
    }

    sealed class Output {
        data class FileList(val items: List<String>)
    }

    data class Item(
        val fileName: String,
        val isCurrentItem: Boolean
    )
}