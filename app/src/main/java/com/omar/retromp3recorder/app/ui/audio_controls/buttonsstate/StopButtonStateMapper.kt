package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class StopButtonStateMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
) {
    fun observe(): Observable<InteractiveButton.State> {
        return audioStateMapper.observe().map { audioState ->
            when (audioState) {
                is AudioState.Recording -> InteractiveButton.State.ENABLED
                is AudioState.Playing -> InteractiveButton.State.ENABLED
                is AudioState.Idle -> InteractiveButton.State.DISABLED
                is AudioState.Paused -> InteractiveButton.State.ENABLED
                is AudioState.Seek_Paused -> InteractiveButton.State.ENABLED
            }
        }
    }
}