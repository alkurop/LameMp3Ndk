package com.omar.retromp3recorder.app.ui.log

import com.github.alkurop.ghostinshell.Shell
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

object LogOutputMapper {
    fun mapOutputToState(): ObservableTransformer<LogView.Output, LogView.State> =
        ObservableTransformer { upstream: Observable<LogView.Output> ->
            upstream.scan(
                getDefaultViewModel(),
                getMapper()
            )
        }

    private fun getMapper(): BiFunction<LogView.State, LogView.Output, LogView.State> =
        BiFunction { oldState: LogView.State, output: LogView.Output ->
            when (output) {
                is LogView.Output.MessageLogOutput -> oldState.copy(
                    message = Shell(output.message),
                )
                is LogView.Output.ErrorLogOutput -> oldState.copy(
                    error = Shell(output.error),
                )
            }
        }

    private fun getDefaultViewModel() = LogView.State(
        error = Shell.empty(),
        message = Shell.empty(),
    )
}