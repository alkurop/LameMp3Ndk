package com.omar.retromp3recorder.app.common.usecase

import com.omar.retromp3recorder.app.common.repo.StateRepo
import com.omar.retromp3recorder.app.main.MainView
import com.omar.retromp3recorder.app.playback.usecase.StopPlaybackUC
import com.omar.retromp3recorder.app.recording.usecase.StopRecordUC
import io.reactivex.Completable
import javax.inject.Inject

class StopPlaybackAndRecordUC @Inject constructor(
    private val stopRecordUC: StopRecordUC,
    private val stopPlaybackUC: StopPlaybackUC,
    private val stateRepo: StateRepo
) {
    fun execute(): Completable = stateRepo.observe().take(1)
        .flatMapCompletable { state: MainView.State ->
            when (state) {
                MainView.State.Playing -> stopPlaybackUC.execute()
                MainView.State.Recording -> stopRecordUC.execute()
                MainView.State.Idle -> Completable.complete()
            }
        }
}
