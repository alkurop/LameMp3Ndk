package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateMapper
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import com.omar.retromp3recorder.utils.FileEmptyChecker
import io.reactivex.Observable
import javax.inject.Inject

class PlayButtonStateMapper @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val audioStateMapper: AudioStateMapper,
    private val fileEmptyChecker: FileEmptyChecker
) {
    fun observe(): Observable<InteractiveButton.State> {
        return Observable.combineLatest(
            currentFileRepo.observe(),
            audioStateMapper.observe(), { currentFile, audioState ->
                when (audioState) {
                    is AudioState.Recording -> InteractiveButton.State.DISABLED
                    is AudioState.Playing -> InteractiveButton.State.RUNNING
                    is AudioState.Idle -> {
                        val hasPlayableFile = fileEmptyChecker.isFileEmpty(currentFile.value).not()
                        if (hasPlayableFile) InteractiveButton.State.ENABLED else InteractiveButton.State.DISABLED
                    }
                }
            }
        )
    }
}