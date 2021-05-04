package com.omar.retromp3recorder.app.ui.files.preview.buttonstate

import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.state.repos.FileListRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class OpenFileButtonStateMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val fileListRepo: FileListRepo
) {
    fun observe(): Observable<Boolean> {
        return Observable.combineLatest(
            fileListRepo.observe(),
            audioStateMapper.observe(),
            { fileList, audioState ->
                when (audioState) {
                    is AudioState.Recording,
                    is AudioState.Playing -> false
                    is AudioState.Idle -> fileList.isNotEmpty()
                }
            }
        )
    }
}