package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import com.omar.retromp3recorder.utils.FileEmptyChecker
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PlayButtonStateMapper @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val audioStateMapper: AudioStateMapper,
    private val fileEmptyChecker: FileEmptyChecker
) {
    fun observe(): Observable<InteractiveButton.State> =
        Observable.combineLatest(
            currentFileRepo.observe(),
            audioStateMapper.observe(), { currentFile, audioState ->
                when (audioState) {
                    is AudioState.Recording -> InteractiveButton.State.DISABLED
                    is AudioState.Paused -> InteractiveButton.State.ENABLED
                    is AudioState.Seek_Paused,
                    is AudioState.Playing -> InteractiveButton.State.RUNNING
                    is AudioState.Idle -> {
                        val path = currentFile.value
                        val hasPlayableFile =
                            path != null && fileEmptyChecker.isFileEmpty(path).not()
                        if (hasPlayableFile) InteractiveButton.State.ENABLED else InteractiveButton.State.DISABLED
                    }
                }
            }
        )
}
