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
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<AudioControlsView.Output> = {
        Observable.merge(listOf(
            audioStateRepo.observe()
                .map { AudioControlsView.Output.AudioStateChanged(it) },
        ))
    }

    private val mapInputToUsecase: (Observable<AudioControlsView.Input>) -> Completable = { input ->
        Completable.merge(listOf(
            input.ofType(AudioControlsView.Input.Play::class.java)
                .flatMapCompletable { startPlaybackUC.execute() },
            input.ofType(AudioControlsView.Input.Stop::class.java)
                .flatMapCompletable { stopPlaybackAndRecordUC.execute() },
            input.ofType(AudioControlsView.Input.Record::class.java)
                .flatMapCompletable { startRecordUC.execute() },
            input.ofType(AudioControlsView.Input.Share::class.java)
                .flatMapCompletable { shareUC.execute() }
        ))
    }
}



