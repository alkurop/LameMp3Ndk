package com.omar.retromp3recorder.app.common.usecase

import com.omar.retromp3recorder.app.common.repo.AudioState
import com.omar.retromp3recorder.app.common.repo.AudioStateRepo
import com.omar.retromp3recorder.app.playback.usecase.StopPlaybackUC
import com.omar.retromp3recorder.app.recording.usecase.StopRecordUC
import io.reactivex.Completable
import javax.inject.Inject

class StopPlaybackAndRecordUC @Inject constructor(
    private val stopRecordUC: StopRecordUC,
    private val stopPlaybackUC: StopPlaybackUC,
    private val stateRepo: AudioStateRepo
) {
    fun execute(): Completable = stateRepo
        .observe()
        .take(1)
        .flatMapCompletable { state: AudioState ->
            when (state) {
                AudioState.Playing -> stopPlaybackUC.execute()
                AudioState.Recording -> stopRecordUC.execute()
                AudioState.Idle -> Completable.complete()
            }
        }
}
