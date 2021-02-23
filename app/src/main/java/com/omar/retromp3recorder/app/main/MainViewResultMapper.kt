package com.omar.retromp3recorder.app.main

import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder
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
                    message = result.message,
                    error = null,
                    playerId = null,
                    requestForPermissions = null
                )
                is MainView.Result.ErrorLogResult -> oldState.copy(
                    message = null,
                    error = result.error,
                    playerId = null,
                    requestForPermissions = null
                )
                is MainView.Result.BitrateChangedResult -> oldState.copy(
                    bitRate = result.bitRate,
                    message = null,
                    error = null,
                    playerId = null,
                    requestForPermissions = null
                )
                is MainView.Result.SampleRateChangeResult -> oldState.copy(
                    sampleRate = result.sampleRate,
                    message = null,
                    error = null,
                    playerId = null,
                    requestForPermissions = null
                )
                is MainView.Result.StateChangedResult -> oldState.copy(
                    state = result.state,
                    message = null,
                    error = null,
                    playerId = null,
                    requestForPermissions = null
                )
                is MainView.Result.RequestPermissionsResult -> oldState.copy(
                    message = null,
                    error = null,
                    playerId = null,
                    requestForPermissions = result.permissionsToRequest
                )
                is MainView.Result.PlayerIdResult -> oldState.copy(
                    message = null,
                    error = null,
                    playerId = result.playerId,
                    requestForPermissions = null
                )
            }
        }

    private fun getDefaultViewModel() = MainView.MainViewModel(
        state = MainView.State.Idle,
        sampleRate = VoiceRecorder.SampleRate._44100,
        bitRate = VoiceRecorder.BitRate._320,
        error = null,
        message = null,
        playerId = null,
        requestForPermissions = null
    )
}