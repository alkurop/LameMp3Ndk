package com.omar.retromp3recorder.app.ui.files.edit.selector

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

object SelectorOutputMapper {
    fun mapOutputToState(): ObservableTransformer<SelectorView.Output, SelectorView.State> =
        ObservableTransformer { upstream: Observable<SelectorView.Output> ->
            upstream.scan(
                getDefaultViewModel(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<SelectorView.State, SelectorView.Output, SelectorView.State> =
        BiFunction { oldState: SelectorView.State, output: SelectorView.Output ->
            when (output) {
                is SelectorView.Output.FileList -> {
                    val selectedFile = oldState.items.firstOrNull { it.isCurrentItem }?.filePath
                    val items = output.items.map { SelectorView.Item(it, it == selectedFile) }
                    oldState.copy(items = items)
                }
                is SelectorView.Output.CurrentFile -> {
                    val selectedFile = output.filePath
                    oldState.copy(
                        items = oldState.items.map { item ->
                            val isSelected = item.filePath == selectedFile
                            SelectorView.Item(item.filePath, isSelected)
                        }
                    )
                }
            }
        }

    private fun getDefaultViewModel() = SelectorView.State(
        items = emptyList()
    )
}