package com.omar.retromp3recorder.app.ui.files.preview.buttonstate

import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.bl.files.CurrentFileMapper
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RenameFileButtonStateMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val currentFileMapper: CurrentFileMapper
) {
    fun observe(): Observable<Boolean> {
        return Observable.combineLatest(
            currentFileMapper.observe(),
            audioStateMapper.observe(),
            { currentFile, audioState ->
                when (audioState) {
                    is AudioState.Idle -> currentFile.value != null
                    else -> false
                }
            }
        )
    }
}