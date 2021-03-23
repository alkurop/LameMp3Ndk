package com.omar.retromp3recorder.app.ui.visualizer

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.state.repos.AudioState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

object VisualizerOutputMapper {
    internal fun mapOutputToState(): ObservableTransformer<VisualizerView.Output, VisualizerView.State> =
        ObservableTransformer { upstream: Observable<VisualizerView.Output> ->
            upstream.scan(defaultViewModel, mapper)
        }

    private val mapper: BiFunction<VisualizerView.State, VisualizerView.Output, VisualizerView.State> =
        BiFunction { oldState: VisualizerView.State, result: VisualizerView.Output ->
            when (result) {
                is VisualizerView.Output.AudioStateChanged -> oldState.copy(
                    audioState = result.state
                )
                is VisualizerView.Output.PlayerIdOutput -> oldState.copy(
                    playerId = Shell(result.playerId)
                )
            }
        }

    private val defaultViewModel = VisualizerView.State(
        audioState = AudioState.Idle(false),
        playerId = Shell.empty()
    )
}