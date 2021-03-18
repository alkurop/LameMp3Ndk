package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.state.AudioState

object AudioControlsView {

    sealed class Input {
        object Play : Input()
        object Record : Input()
        object Share : Input()
        object Stop : Input()
    }

    sealed class Output {
        data class StateChanged(val state:  AudioState) : Output()
    }

    data class State(
        val audioState: AudioState
    )
}