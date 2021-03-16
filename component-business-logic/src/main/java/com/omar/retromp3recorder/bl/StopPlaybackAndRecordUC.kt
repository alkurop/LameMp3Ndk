package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.state.AudioState
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
        .flatMapCompletable { state: AudioState ->
            when (state) {
                AudioState.Playing -> stopPlaybackUC.execute()
                AudioState.Recording -> stopRecordUC.execute()
                AudioState.Idle -> Completable.complete()
            }
        }
}
