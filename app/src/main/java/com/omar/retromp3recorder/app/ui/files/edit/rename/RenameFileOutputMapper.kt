package com.omar.retromp3recorder.app.ui.files.edit.rename

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object RenameFileOutputMapper {
    fun mapOutputToState(): ObservableTransformer<RenameFileView.Output, RenameFileView.State> =
        ObservableTransformer { upstream: Observable<RenameFileView.Output> ->
            upstream.scan(
                RenameFileView.State(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<RenameFileView.State, RenameFileView.Output, RenameFileView.State> =
        BiFunction { oldState: RenameFileView.State, output: RenameFileView.Output ->
            oldState.copy()
        }
}