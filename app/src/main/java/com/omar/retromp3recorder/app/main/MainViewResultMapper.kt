package com.omar.retromp3recorder.app.main

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.app.common.repo.AudioState
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

object MainViewResultMapper {
    fun map(): ObservableTransformer<MainView.Result, MainView.MainViewModel> =
        ObservableTransformer { upstream: Observable<MainView.Result> ->
            upstream
                .scan(
                    getDefaultViewModel(),
                    getMapper()
                )
        }

    private fun getMapper(): BiFunction<MainView.MainViewModel, MainView.Result, MainView.MainViewModel> =
        BiFunction { oldState: MainView.MainViewModel, result: MainView.Result ->
            when (result) {
                is MainView.Result.MessageLogResult -> oldState.copy(
                    message = Shell(result.message),
                )
                is MainView.Result.ErrorLogResult -> oldState.copy(
                    error = Shell(result.error),
                )
                is MainView.Result.BitrateChangedResult -> oldState.copy(
                    bitRate = result.bitRate,
                )
                is MainView.Result.SampleRateChangeResult -> oldState.copy(
                    sampleRate = result.sampleRate,
                )
                is MainView.Result.StateChangedResult -> oldState.copy(
                    state = result.state,
                )
                is MainView.Result.RequestPermissionsResult -> oldState.copy(
                    requestForPermissions = Shell(result.permissionsToRequest)
                )
                is MainView.Result.PlayerIdResult -> oldState.copy(
                    playerId = Shell(result.playerId),
                )
            }
        }

    private fun getDefaultViewModel() = MainView.MainViewModel(
        state = MainView.State.Idle,
        sampleRate = Mp3VoiceRecorder.SampleRate._44100,
        bitRate = Mp3VoiceRecorder.BitRate._320,
        error = Shell.empty(),
        message = Shell.empty(),
        playerId = Shell.empty(),
        requestForPermissions = Shell.empty()
    )
}

fun AudioState.map(): MainView.State = when (this) {
    AudioState.Idle -> MainView.State.Idle
    AudioState.Playing -> MainView.State.Playing
    AudioState.Recording -> MainView.State.Recording
}