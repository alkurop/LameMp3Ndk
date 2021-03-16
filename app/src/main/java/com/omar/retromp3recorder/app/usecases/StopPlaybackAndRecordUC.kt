package com.omar.retromp3recorder.app.usecases

import io.reactivex.Completable
import javax.inject.Inject

class StopPlaybackAndRecordUC @Inject constructor(
    private val stopRecordUC: StopRecordUC,
    private val stopPlaybackUC: StopPlaybackUC,
    private val stateRepo: com.omar.retromp3recorder.state.AudioStateRepo
) {
    fun execute(): Completable = stateRepo
        .observe()
        .take(1)
        .flatMapCompletable { state: com.omar.retromp3recorder.state.AudioState ->
            when (state) {
                com.omar.retromp3recorder.state.AudioState.Playing -> stopPlaybackUC.execute()
                com.omar.retromp3recorder.state.AudioState.Recording -> stopRecordUC.execute()
                com.omar.retromp3recorder.state.AudioState.Idle -> Completable.complete()
            }
        }
}
