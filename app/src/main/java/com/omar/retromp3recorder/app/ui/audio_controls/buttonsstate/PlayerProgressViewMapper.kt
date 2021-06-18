package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.bl.audio.PlayerProgressMapper
import com.omar.retromp3recorder.bl.files.HasPlayableFileMapper
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PlayerProgressViewMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val playerProgressMapper: PlayerProgressMapper,
    private val hasPlayableFileMapper: HasPlayableFileMapper
) {
    fun observe(): Observable<PlayerProgressViewState> = Observable.combineLatest(
        hasPlayableFileMapper.observe(),
        audioStateMapper.observe(),
        playerProgressMapper.observe(),
        { hasFile, audioState, progress ->
            when (audioState) {
                is AudioState.Recording,
                is AudioState.Seek_Paused ->
                    PlayerProgressViewState.Hidden
                is AudioState.Playing -> {
                    val unwrap = progress.value
                    if (unwrap == null || hasFile) PlayerProgressViewState.Hidden
                    else PlayerProgressViewState.Visible(unwrap.first, unwrap.second)
                }
                is AudioState.Idle -> {
                    val unwrap = progress.value
                    if (unwrap == null || hasFile) PlayerProgressViewState.Hidden
                    else PlayerProgressViewState.Visible(unwrap.first, unwrap.second)
                }
            }
        })
}

sealed class PlayerProgressViewState {
    object Hidden : PlayerProgressViewState()
    data class Visible(val progress: Int, val duration: Int) : PlayerProgressViewState()
}