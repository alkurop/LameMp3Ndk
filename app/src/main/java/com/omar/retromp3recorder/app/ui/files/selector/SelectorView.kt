package com.omar.retromp3recorder.app.ui.files.selector

import com.omar.retromp3recorder.dto.ExistingFileWrapper

object SelectorView {
    data class State(
        val selectedFile: String?,
        val items: List<Item>
    )

    sealed class Input {
        data class ItemSelected(val item: Item) : Input()
    }

    sealed class Output {
        data class FileList(val items: List<ExistingFileWrapper>) : Output()
        data class CurrentFile(val filePath: String?) : Output()
    }

    data class Item(
        val fileWrapper: ExistingFileWrapper,
        val isCurrentItem: Boolean
    )
}