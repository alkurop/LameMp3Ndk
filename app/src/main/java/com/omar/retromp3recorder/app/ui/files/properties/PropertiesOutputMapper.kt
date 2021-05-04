package com.omar.retromp3recorder.app.ui.files.properties

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object PropertiesOutputMapper {
    fun mapOutputToState(): ObservableTransformer<PropertiesView.Output, PropertiesView.State> =
        ObservableTransformer { upstream: Observable<PropertiesView.Output> ->
            upstream.scan(
                getDefaultViewModel(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<PropertiesView.State, PropertiesView.Output, PropertiesView.State> =
        BiFunction { oldState: PropertiesView.State, output: PropertiesView.Output ->
            oldState.copy()
        }

    private fun getDefaultViewModel() = PropertiesView.State(
    )
}