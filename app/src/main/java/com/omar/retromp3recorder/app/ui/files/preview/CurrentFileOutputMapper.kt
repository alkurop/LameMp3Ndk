package com.omar.retromp3recorder.app.ui.files.preview

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

object CurrentFileOutputMapper {
    fun mapOutputToState(): ObservableTransformer<CurrentFileView.Output, CurrentFileView.State> =
        ObservableTransformer { upstream: Observable<CurrentFileView.Output> ->
            upstream.scan(
                getDefaultViewModel(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<CurrentFileView.State, CurrentFileView.Output, CurrentFileView.State> =
        BiFunction { oldState: CurrentFileView.State, output: CurrentFileView.Output ->
            when (output) {
                is CurrentFileView.Output.CurrentFileOutput -> {
                    oldState.copy(
                        currentFilePath = output.currentFilePath
                    )
                }
                is CurrentFileView.Output.AudioInactive -> {
                    oldState.copy(isShowingFileButtons = true)
                }
                is CurrentFileView.Output.AudioActive -> {
                    oldState.copy(isShowingFileButtons = false)
                }
            }
        }

    private fun getDefaultViewModel() = CurrentFileView.State(
        currentFilePath = null,
        isShowingFileButtons = false
    )
}