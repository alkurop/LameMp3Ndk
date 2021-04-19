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
            oldState.copy()
        }

    private fun getDefaultViewModel() = SelectorView.State(
    )
}