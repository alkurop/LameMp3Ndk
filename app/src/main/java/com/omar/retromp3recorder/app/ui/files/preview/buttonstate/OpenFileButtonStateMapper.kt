package com.omar.retromp3recorder.app.ui.files.preview.buttonstate

import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.storage.repo.FileListRepo
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
                    is AudioState.Idle -> fileList.isNotEmpty()
                    else -> false
                }
            }
        )
    }
}