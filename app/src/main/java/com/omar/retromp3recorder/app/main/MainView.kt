package com.omar.retromp3recorder.app.main

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.BitRate
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.SampleRate

object MainView {
    enum class State {
        Idle,
        Playing,
        Recording
    }

    data class MainViewModel(
        val bitRate: BitRate,
        val error: Shell<String>,
        val message: Shell<String>,
        val playerId: Shell<Int>,
        val requestForPermissions: Shell<Set<String>>,
        val state: State,
        val sampleRate: SampleRate
    )

    sealed class Action {
        data class BitRateChange(val bitRate: BitRate) : Action()
        object Play : Action()
        object Record : Action()
        data class SampleRateChange(val sampleRate: SampleRate) : Action()
        object Share : Action()
        object Stop : Action()
    }

    sealed class Result {
        data class BitrateChangedResult(val bitRate: BitRate) : Result()
        data class ErrorLogResult(val error: String) : Result()
        data class MessageLogResult(val message: String) : Result()
        data class PlayerIdResult(val playerId: Int) : Result()
        data class RequestPermissionsResult(val permissionsToRequest: Set<String>) : Result()
        data class SampleRateChangeResult(val sampleRate: SampleRate) : Result()
        data class StateChangedResult(val state: State) : Result()
    }
}