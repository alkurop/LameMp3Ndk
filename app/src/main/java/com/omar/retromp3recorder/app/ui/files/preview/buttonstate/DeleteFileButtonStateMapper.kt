package com.omar.retromp3recorder.app.ui.files.preview.buttonstate

import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateMapper
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import io.reactivex.Observable
import javax.inject.Inject

class DeleteFileButtonStateMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val currentFileRepo: CurrentFileRepo
) {
    fun observe(): Observable<Boolean> {
        return Observable.combineLatest(
            currentFileRepo.observe(),
            audioStateMapper.observe(),
            { currentFile, audioState ->
                when (audioState) {
                    is AudioState.Recording,
                    is AudioState.Playing -> false
                    is AudioState.Idle -> currentFile.value != null
                }
            }
        )
    }
}