package com.omar.retromp3recorder.app.ui.files.rename

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.BiFunction

object RenameFileView {
    data class State(
        val shouldDismiss: Boolean = false,
        val isOkButtonEnabled: Boolean = false,
        val fileWrapper: Shell<ExistingFileWrapper> = Shell.empty()
    )

    sealed class Input {
        data class CheckCanRename(
            val newName: String
        ) : Input()

        data class Rename(
            val newName: String
        ) : Input()
    }

    sealed class Output {
        data class CanDismiss(val isDismissed: Boolean) : Output()
        data class OkButtonState(val isEnabled: Boolean) : Output()
        data class CurrentFile(val fileWrapper: ExistingFileWrapper) : Output()
    }
}

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
            when (output) {
                is RenameFileView.Output.CanDismiss -> oldState.copy(shouldDismiss = output.isDismissed)
                is RenameFileView.Output.OkButtonState -> oldState.copy(isOkButtonEnabled = output.isEnabled)
                is RenameFileView.Output.CurrentFile -> oldState.copy(fileWrapper = Shell(output.fileWrapper))
            }
        }
}