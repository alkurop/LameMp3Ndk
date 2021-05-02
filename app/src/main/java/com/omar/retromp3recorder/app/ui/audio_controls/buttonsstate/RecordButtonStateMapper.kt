package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateMapper
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RecordButtonStateMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
) {
    fun observe(): Observable<InteractiveButton.State> {
        return audioStateMapper.observe().map { audioState ->
            when (audioState) {
                is AudioState.Recording -> InteractiveButton.State.RUNNING
                is AudioState.Playing -> InteractiveButton.State.DISABLED
                is AudioState.Idle -> InteractiveButton.State.ENABLED
            }
        }
    }
}