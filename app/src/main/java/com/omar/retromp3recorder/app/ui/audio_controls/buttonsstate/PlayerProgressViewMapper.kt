package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.bl.audio.PlayerProgressMapper
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PlayerProgressViewMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val playerProgressMapper: PlayerProgressMapper
) {
    fun observe(): Observable<PlayerProgressViewState> = audioStateMapper
        .observe()
        .switchMap { audioState ->
            when (audioState) {
                is AudioState.Recording,
                is AudioState.Idle,
                is AudioState.Seek_Paused ->
                    Observable.just(PlayerProgressViewState.Hidden)
                is AudioState.Playing ->
                    playerProgressMapper.observe().map {
                        val progress = it.value
                        if (progress != null) {
                            PlayerProgressViewState.Visible(progress.first, progress.second)
                        } else {
                            PlayerProgressViewState.Hidden
                        }
                    }
            }
        }
}

sealed class PlayerProgressViewState {
    object Hidden : PlayerProgressViewState()
    data class Visible(val progress: Int, val duration: Int) : PlayerProgressViewState()
}