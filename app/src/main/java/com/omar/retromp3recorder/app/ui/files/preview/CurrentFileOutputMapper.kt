package com.omar.retromp3recorder.app.ui.files.preview

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.R
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
                    val fileName = output.currentFileName
                    oldState.copy(
                        currentFileName =
                        if (fileName != null) Stringer.ofString(fileName)
                        else Stringer(R.string.no_file)
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
        currentFileName = Stringer(R.string.no_file),
        isShowingFileButtons = false
    )
}