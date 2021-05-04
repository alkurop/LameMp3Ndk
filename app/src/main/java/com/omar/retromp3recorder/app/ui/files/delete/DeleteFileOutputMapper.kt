package com.omar.retromp3recorder.app.ui.files.delete

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

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
            when (output) {
                is DeleteFileView.Output.Finished ->
                    oldState.copy(shouldDismiss = true)
            }
        }

    private fun getDefaultViewModel() = DeleteFileView.State(
        shouldDismiss = false
    )
}