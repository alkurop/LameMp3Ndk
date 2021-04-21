package com.omar.retromp3recorder.app.ui.files.edit.delete

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

object DeleteFileOutputMapper {
    fun mapOutputToState(): ObservableTransformer<DeleteFileView.Output, DeleteFileView.State> =
        ObservableTransformer { upstream: Observable<DeleteFileView.Output> ->
            upstream.scan(
                getDefaultViewModel(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<DeleteFileView.State, DeleteFileView.Output, DeleteFileView.State> =
        BiFunction { oldState: DeleteFileView.State, output: DeleteFileView.Output ->
            oldState.copy()
        }

    private fun getDefaultViewModel() = DeleteFileView.State(
    )
}