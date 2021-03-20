package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.state.repos.AudioState
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction

fun mapOutputToState(): ObservableTransformer<AudioControlsView.Output, AudioControlsView.State> =
    ObservableTransformer { upstream: Observable<AudioControlsView.Output> ->
        upstream.scan(defaultViewModel, mapper)
    }

private val mapper : BiFunction<AudioControlsView.State, AudioControlsView.Output, AudioControlsView.State> =
    BiFunction { oldState: AudioControlsView.State, result: AudioControlsView.Output ->
        when (result) {
            is AudioControlsView.Output.AudioStateChanged -> oldState.copy(
                audioState = result.state
            )
            is AudioControlsView.Output.AudioFileFound -> oldState.copy(
                hasAudioFile = true
            )
        }
    }

private val defaultViewModel = AudioControlsView.State(
    audioState = AudioState.Idle,
    hasAudioFile = false
)
