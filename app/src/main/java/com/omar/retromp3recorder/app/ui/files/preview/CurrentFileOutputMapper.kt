package com.omar.retromp3recorder.app.ui.files.preview

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object CurrentFileOutputMapper {
    fun mapOutputToState(): ObservableTransformer<CurrentFileView.Output, CurrentFileView.State> =
        ObservableTransformer { upstream: Observable<CurrentFileView.Output> ->
            upstream.scan(
                CurrentFileView.State(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<CurrentFileView.State, CurrentFileView.Output, CurrentFileView.State> =
        BiFunction { oldState: CurrentFileView.State, output: CurrentFileView.Output ->
            when (output) {
                is CurrentFileView.Output.CurrentFileOutput -> oldState.copy(
                    currentFilePath = output.currentFilePath
                )
                is CurrentFileView.Output.DeleteButtonState -> oldState.copy(
                    isDeleteFileActive = output.isActive,
                )
                is CurrentFileView.Output.OpenButtonState -> oldState.copy(
                    isOpenFileActive = output.isActive,
                )
            }
        }
}