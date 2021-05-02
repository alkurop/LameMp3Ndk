package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateMapper
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import com.omar.retromp3recorder.utils.FileEmptyChecker
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class ShareButtonStateMapper @Inject constructor(
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
                    is AudioState.Playing -> InteractiveButton.State.DISABLED
                    is AudioState.Idle -> {
                        val path = currentFile.value
                        val hasPlayableFile =
                            path != null && fileEmptyChecker.isFileEmpty(
                                path
                            ).not()
                        if (hasPlayableFile) InteractiveButton.State.ENABLED else InteractiveButton.State.DISABLED
                    }
                }
            }
        )
    }
}