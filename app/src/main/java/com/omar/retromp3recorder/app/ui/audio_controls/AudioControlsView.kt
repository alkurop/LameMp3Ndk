package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.state.AudioState

object AudioControlsView {

    sealed class Action {
        object Play : Action()
        object Record : Action()
        object Share : Action()
        object Stop : Action()
    }

    sealed class Result {
        data class StateChanged(val state:  AudioState) : Result()
    }

    data class State(
        val audioState: AudioState
    )
}