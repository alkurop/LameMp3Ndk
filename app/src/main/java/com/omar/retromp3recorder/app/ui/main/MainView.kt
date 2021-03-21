package com.omar.retromp3recorder.app.ui.main

import com.github.alkurop.ghostinshell.Shell
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.AudioState

object MainView {
    data class State(
        val audioState: AudioState,
        val bitRate: Mp3VoiceRecorder.BitRate,
        val error: Shell<Stringer>,
        val message: Shell<Stringer>,
        val playerId: Shell<Int>,
        val requestForPermissions: Shell<Set<String>>,
        val sampleRate: Mp3VoiceRecorder.SampleRate
    )

    sealed class Input {
        data class BitRateChange(val bitRate: Mp3VoiceRecorder.BitRate) : Input()
        data class SampleRateChange(val sampleRate: Mp3VoiceRecorder.SampleRate) : Input()
    }

    sealed class Output {
        data class BitrateChangedOutput(val bitRate: Mp3VoiceRecorder.BitRate) : Output()
        data class ErrorLogOutput(val error: Stringer) : Output()
        data class MessageLogOutput(val message: Stringer) : Output()
        data class PlayerIdOutput(val playerId: Int) : Output()
        data class RequestPermissionsOutput(val permissionsToRequest: Set<String>) : Output()
        data class SampleRateChangeOutput(val sampleRate: Mp3VoiceRecorder.SampleRate) : Output()
        data class StateChangedOutput(val state: AudioState) : Output()
    }
}