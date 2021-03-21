package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.bl.ShareUC
import com.omar.retromp3recorder.bl.StartPlaybackUC
import com.omar.retromp3recorder.bl.StartRecordUC
import com.omar.retromp3recorder.bl.StopPlaybackAndRecordUC
import com.omar.retromp3recorder.state.repos.AudioStateRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import javax.inject.Inject

class AudioControlsInteractor @Inject constructor(
    private val audioStateRepo: AudioStateRepo,
    private val startRecordUC: StartRecordUC,
    private val shareUC: ShareUC,
    private val startPlaybackUC: StartPlaybackUC,
    private val stopPlaybackAndRecordUC: StopPlaybackAndRecordUC,
    private val workScheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<AudioControlsView.Input, AudioControlsView.Output> =
        workScheduler.processIO(
            outMapper = usecaseMapper,
            inMappper = mapStateToResult
        )

    private val mapStateToResult: () -> Observable<AudioControlsView.Output> = {
        Observable.merge(listOf(
            audioStateRepo.observe()
                .map { AudioControlsView.Output.AudioStateChanged(it) },
        ))
    }

    private val usecaseMapper: (Observable<AudioControlsView.Input>) -> Completable = { actions ->
        Completable.merge(listOf(
            actions.ofType(AudioControlsView.Input.Play::class.java)
                .flatMapCompletable { startPlaybackUC.execute() },
            actions.ofType(AudioControlsView.Input.Stop::class.java)
                .flatMapCompletable { stopPlaybackAndRecordUC.execute() },
            actions.ofType(AudioControlsView.Input.Record::class.java)
                .flatMapCompletable { startRecordUC.execute() },
            actions.ofType(AudioControlsView.Input.Share::class.java)
                .flatMapCompletable { shareUC.execute() }
        ))
    }
}



