package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.PlayButtonStateMapper
import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.RecordButtonStateMapper
import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.ShareButtonStateMapper
import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.StopButtonStateMapper
import com.omar.retromp3recorder.bl.ShareUC
import com.omar.retromp3recorder.bl.StartPlaybackUC
import com.omar.retromp3recorder.bl.StartRecordUC
import com.omar.retromp3recorder.bl.StopPlaybackAndRecordUC
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class AudioControlsInteractor @Inject constructor(
    private val playButtonStateMapper: PlayButtonStateMapper,
    private val recordButtonStateMapper: RecordButtonStateMapper,
    private val shareButtonStateMapper: ShareButtonStateMapper,
    private val stopButtonStateMapper: StopButtonStateMapper,
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
        Observable.merge(
            listOf(
                playButtonStateMapper.observe()
                    .map { AudioControlsView.Output.PlayButtonState(it) },
                recordButtonStateMapper.observe()
                    .map { AudioControlsView.Output.RecordButtonState(it) },
                stopButtonStateMapper.observe()
                    .map { AudioControlsView.Output.StopButtonState(it) },
                shareButtonStateMapper.observe()
                    .map { AudioControlsView.Output.ShareButtonState(it) },
            )
        )
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



