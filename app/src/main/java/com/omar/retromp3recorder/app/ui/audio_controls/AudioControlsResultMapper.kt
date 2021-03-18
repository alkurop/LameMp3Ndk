package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.state.AudioState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

object AudioControlsResultMapper {
    fun mapResultToState(): ObservableTransformer<AudioControlsView.Result, AudioControlsView.State> =
        ObservableTransformer { upstream: Observable<AudioControlsView.Result> ->
            upstream
                .scan(
                    getDefaultViewModel(),
                    getMapper()
                )
        }

    private fun getMapper(): BiFunction<AudioControlsView.State, AudioControlsView.Result,AudioControlsView.State> =
        BiFunction { oldState: AudioControlsView.State, result: AudioControlsView.Result ->
            when (result) {
                is AudioControlsView.Result.StateChanged -> oldState.copy(
                    audioState = result.state
                )
            }
        }

    private fun getDefaultViewModel() = AudioControlsView.State(
        audioState = AudioState.Idle
    )
}
