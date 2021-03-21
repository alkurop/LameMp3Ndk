package com.omar.retromp3recorder.app.ui.main

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.AudioState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

object MainViewResultMapper {
    fun map(): ObservableTransformer<MainView.Output, MainView.State> =
        ObservableTransformer { upstream: Observable<MainView.Output> ->
            upstream
                .scan(
                    getDefaultViewModel(),
                    getMapper()
                )
        }

    private fun getMapper(): BiFunction<MainView.State, MainView.Output, MainView.State> =
        BiFunction { oldState: MainView.State, output: MainView.Output ->
            when (output) {
                is MainView.Output.MessageLogOutput -> oldState.copy(
                    message = Shell(output.message),
                )
                is MainView.Output.ErrorLogOutput -> oldState.copy(
                    error = Shell(output.error),
                )
                is MainView.Output.BitrateChangedOutput -> oldState.copy(
                    bitRate = output.bitRate,
                )
                is MainView.Output.SampleRateChangeOutput -> oldState.copy(
                    sampleRate = output.sampleRate,
                )
                is MainView.Output.StateChangedOutput -> oldState.copy(
                    audioState = output.state,
                )
                is MainView.Output.RequestPermissionsOutput -> oldState.copy(
                    requestForPermissions = Shell(output.permissionsToRequest)
                )
                is MainView.Output.PlayerIdOutput -> oldState.copy(
                    playerId = Shell(output.playerId),
                )
            }
        }

    private fun getDefaultViewModel() = MainView.State(
        audioState = AudioState.Idle(false),
        sampleRate = Mp3VoiceRecorder.SampleRate._44100,
        bitRate = Mp3VoiceRecorder.BitRate._320,
        error = Shell.empty(),
        message = Shell.empty(),
        playerId = Shell.empty(),
        requestForPermissions = Shell.empty()
    )
}
