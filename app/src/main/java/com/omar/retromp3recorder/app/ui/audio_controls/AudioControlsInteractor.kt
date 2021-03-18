package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.bl.ShareUC
import com.omar.retromp3recorder.bl.StartPlaybackUC
import com.omar.retromp3recorder.bl.StartRecordUC
import com.omar.retromp3recorder.bl.StopPlaybackAndRecordUC
import com.omar.retromp3recorder.state.AudioStateRepo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import javax.inject.Inject

class AudioControlsInteractor @Inject constructor(
    private val scheduler: Scheduler,
    private val startRecordUC: StartRecordUC,
    private val shareUC: ShareUC,
    private val startPlaybackUC: StartPlaybackUC,
    private val stopPlaybackAndRecordUC: StopPlaybackAndRecordUC,
    private val stateRepo: AudioStateRepo
) {
    fun processActions(): ObservableTransformer<AudioControlsView.Action, AudioControlsView.Result> =
        ObservableTransformer { actions ->
            mapStateToResult().mergeWith(
                actions
                    .observeOn(scheduler)
                    .mapToUsecase()
            )
        }


    private fun mapStateToResult(): Observable<AudioControlsView.Result> =
        Observable.merge(listOf(
            stateRepo.observe()
                .map { AudioControlsView.Result.StateChanged(it) }
        ))


    private fun Observable<AudioControlsView.Action>.mapToUsecase(): Completable =
        Completable.merge(listOf(
            this.ofType(AudioControlsView.Action.Play::class.java)
                .flatMapCompletable { startPlaybackUC.execute() },
            this.ofType(AudioControlsView.Action.Stop::class.java)
                .flatMapCompletable { stopPlaybackAndRecordUC.execute() },
            this.ofType(AudioControlsView.Action.Record::class.java)
                .flatMapCompletable { startRecordUC.execute() },
            this.ofType(AudioControlsView.Action.Share::class.java)
                .flatMapCompletable { shareUC.execute() }
        ))
}
