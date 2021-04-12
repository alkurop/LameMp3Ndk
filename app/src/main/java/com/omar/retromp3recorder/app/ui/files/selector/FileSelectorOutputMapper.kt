package com.omar.retromp3recorder.app.ui.files.selector


import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

object FileSelectorOutputMapper {
    fun mapOutputToState(): ObservableTransformer<FileSelectorView.Output, FileSelectorView.State> =
        ObservableTransformer { upstream: Observable<FileSelectorView.Output> ->
            upstream.scan(
                getDefaultViewModel(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<FileSelectorView.State, FileSelectorView.Output, FileSelectorView.State> =
        BiFunction { oldState: FileSelectorView.State, output: FileSelectorView.Output ->
            oldState.copy()
        }

    private fun getDefaultViewModel() = FileSelectorView.State(
    )
}