package com.omar.retromp3recorder.app.ui.visualizer

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.state.repos.AudioState

object VisualizerView {
    data class State(
        val audioState: AudioState,
        val playerId: Shell<Int>
    )

    sealed class Input

    sealed class Output {
        data class PlayerIdOutput(val playerId: Int) : VisualizerView.Output()
        data class StateChangedOutput(val state: AudioState) : VisualizerView.Output()
    }
}