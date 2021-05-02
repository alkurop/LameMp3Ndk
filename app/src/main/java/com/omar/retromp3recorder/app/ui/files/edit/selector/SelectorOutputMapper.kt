package com.omar.retromp3recorder.app.ui.files.edit.selector

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

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
                    val selectedFile = oldState.selectedFile
                    val items =
                        output.items.map { SelectorView.Item(it, it.path == selectedFile) }
                            .reversed()
                    oldState.copy(items = items)
                }
                is SelectorView.Output.CurrentFile -> {
                    val selectedFile = output.filePath

                    oldState.copy(
                        items = oldState.items.map { item ->
                            val isSelected = item.fileWrapper.path == selectedFile
                            SelectorView.Item(item.fileWrapper, isSelected)
                        },
                        selectedFile = selectedFile
                    )
                }
            }
        }

    private fun getDefaultViewModel() = SelectorView.State(
        items = emptyList(),
        // selected file has to be here,   in case selection comes before list
        selectedFile = null
    )
}