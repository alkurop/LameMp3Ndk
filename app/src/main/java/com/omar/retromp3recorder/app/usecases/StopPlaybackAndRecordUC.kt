package com.omar.retromp3recorder.app.usecases

import com.omar.retromp3recorder.app.state.AudioState
import com.omar.retromp3recorder.app.state.AudioStateRepo
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
