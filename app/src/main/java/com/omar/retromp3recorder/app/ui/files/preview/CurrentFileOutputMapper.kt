package com.omar.retromp3recorder.app.ui.files.preview

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.ExistingFileWrapper
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
                is CurrentFileView.Output.CurrentFileOutput -> {
                    val existingFile = output.currentFile as? ExistingFileWrapper

                    oldState.copy(
                        filePath = output.currentFile?.path,
                        wavetable = Shell(existingFile?.wavetable)
                    )
                }
                is CurrentFileView.Output.DeleteButtonState -> oldState.copy(
                    isDeleteFileActive = output.isActive,
                )
                is CurrentFileView.Output.OpenButtonState -> oldState.copy(
                    isOpenFileActive = output.isActive,
                )
                is CurrentFileView.Output.RenameButtonState -> oldState.copy(
                    isRenameButtonActive = output.isActive
                )
                is CurrentFileView.Output.PlayerProgressOut -> {
                    val progress = Shell(output.progress.value)
                    oldState.copy(playerProgress = progress)
                }
                is CurrentFileView.Output.IsRecordingOut -> oldState.copy(
                    isRecording = output.isRecording
                )
            }
        }
}