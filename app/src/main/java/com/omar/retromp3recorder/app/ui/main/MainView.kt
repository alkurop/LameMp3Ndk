package com.omar.retromp3recorder.app.ui.main

import com.github.alkurop.ghostinshell.Shell
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder

object MainView {
    enum class State {
        Idle,
        Playing,
        Recording
    }

    data class MainViewModel(
        val bitRate: Mp3VoiceRecorder.BitRate,
        val error: Shell<Stringer>,
        val message: Shell<Stringer>,
        val playerId: Shell<Int>,
        val requestForPermissions: Shell<Set<String>>,
        val state: State,
        val sampleRate: Mp3VoiceRecorder.SampleRate
    )

    sealed class Action {
        data class BitRateChange(val bitRate: Mp3VoiceRecorder.BitRate) : Action()
        object Play : Action()
        object Record : Action()
        data class SampleRateChange(val sampleRate: Mp3VoiceRecorder.SampleRate) : Action()
        object Share : Action()
        object Stop : Action()
    }

    sealed class Result {
        data class BitrateChangedResult(val bitRate: Mp3VoiceRecorder.BitRate) : Result()
        data class ErrorLogResult(val error: Stringer) : Result()
        data class MessageLogResult(val message: Stringer) : Result()
        data class PlayerIdResult(val playerId: Int) : Result()
        data class RequestPermissionsResult(val permissionsToRequest: Set<String>) : Result()
        data class SampleRateChangeResult(val sampleRate: Mp3VoiceRecorder.SampleRate) : Result()
        data class StateChangedResult(val state: State) : Result()
    }
}