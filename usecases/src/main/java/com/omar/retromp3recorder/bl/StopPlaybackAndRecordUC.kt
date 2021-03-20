package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateRepo
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
