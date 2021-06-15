package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.PlayButtonStateMapper
import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.RecordButtonStateMapper
import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.ShareButtonStateMapper
import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.StopButtonStateMapper
import com.omar.retromp3recorder.bl.ShareUC
import com.omar.retromp3recorder.bl.audio.PlayerProgressMapper
import com.omar.retromp3recorder.bl.audio.StartPlaybackUC
import com.omar.retromp3recorder.bl.audio.StartRecordUC
import com.omar.retromp3recorder.bl.audio.StopPlaybackAndRecordUC
import com.omar.retromp3recorder.utils.mapToUsecase
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class AudioControlsInteractor @Inject constructor(
    private val playButtonStateMapper: PlayButtonStateMapper,
    private val playerProgressMapper: PlayerProgressMapper,
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
        Completable.merge(
            listOf(
                input.mapToUsecase<AudioControlsView.Input.Play> { startPlaybackUC.execute() },
                input.mapToUsecase<AudioControlsView.Input.Stop> { stopPlaybackAndRecordUC.execute() },
                input.mapToUsecase<AudioControlsView.Input.Record> { startRecordUC.execute() },
                input.mapToUsecase<AudioControlsView.Input.Share> { shareUC.execute() },
            )
        )
    }
}



