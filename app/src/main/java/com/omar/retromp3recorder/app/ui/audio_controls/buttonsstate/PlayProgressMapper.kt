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
    fun obsserve(): Observable<PlayerProgress> = audioStateMapper
        .observe()
        .switchMap { audioState ->
            when (audioState) {
                is AudioState.Recording,
                is AudioState.Idle,
                is AudioState.Seek_Paused ->
                    Observable.just(PlayerProgress.Hidden)
                is AudioState.Playing ->
                    playerProgressMapper.observe().map {
                        val progress = it.value
                        if (progress != null) {
                            PlayerProgress.Visible(progress.first, progress.second)
                        } else {
                            PlayerProgress.Hidden
                        }
                    }
            }
        }
}

sealed class PlayerProgress {
    object Hidden : PlayerProgress()
    data class Visible(val progress: Int, val duration: Int) : PlayerProgress()
}