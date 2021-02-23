package com.omar.retromp3recorder.app.main

import com.omar.retromp3recorder.app.common.repo.LogRepo
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo.Denied
import com.omar.retromp3recorder.app.common.repo.StateRepo
import com.omar.retromp3recorder.app.common.usecase.StopPlaybackAndRecordUC
import com.omar.retromp3recorder.app.main.MainView.Result
import com.omar.retromp3recorder.app.playback.repo.PlayerIdRepo
import com.omar.retromp3recorder.app.playback.usecase.StartPlaybackUC
import com.omar.retromp3recorder.app.recording.repo.BitRateRepo
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo
import com.omar.retromp3recorder.app.recording.usecase.ChangeBitrateUC
import com.omar.retromp3recorder.app.recording.usecase.ChangeSampleRateUC
import com.omar.retromp3recorder.app.recording.usecase.StartRecordUC
import com.omar.retromp3recorder.app.share.ShareUC
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import javax.inject.Inject

class MainViewInteractor @Inject constructor(
    private val scheduler: Scheduler,
    private val changeBitrateUC: ChangeBitrateUC,
    private val changeSampleRateUC: ChangeSampleRateUC,
    private val startRecordUC: StartRecordUC,
    private val shareUC: ShareUC,
    private val startPlaybackUC: StartPlaybackUC,
    private val stopPlaybackAndRecordUC: StopPlaybackAndRecordUC,
    private val bitRateRepo: BitRateRepo,
    private val sampleRateRepo: SampleRateRepo,
    private val stateRepo: StateRepo,
    private val requestPermissionsRepo: RequestPermissionsRepo,
    private val logRepo: LogRepo,
    private val playerIdRepo: PlayerIdRepo
) {

    fun process(): ObservableTransformer<MainView.Action, Result> =
        ObservableTransformer { upstream ->
            upstream.observeOn(scheduler)
                .compose { actions ->
                    Observable.merge(
                        actionsMapper(actions).toObservable(),
                        repoMapper()
                    )
                }
        }

    private fun actionsMapper(actions: Observable<MainView.Action>) = Completable.merge(listOf(
        actions.ofType(MainView.Action.Play::class.java)
            .flatMapCompletable { startPlaybackUC.execute() },
        actions.ofType(MainView.Action.Record::class.java)
            .flatMapCompletable { startRecordUC.execute() },
        actions.ofType(MainView.Action.Share::class.java)
            .flatMapCompletable { shareUC.execute() },
        actions.ofType(MainView.Action.Stop::class.java)
            .flatMapCompletable { stopPlaybackAndRecordUC.execute() },
        actions.ofType(MainView.Action.SampleRateChange::class.java)
            .flatMapCompletable { sampleRateChangeAction ->
                changeSampleRateUC.execute(sampleRateChangeAction.sampleRate)
            },
        actions.ofType(MainView.Action.BitRateChange::class.java)
            .flatMapCompletable { bitRateChangeAction ->
                changeBitrateUC.execute(bitRateChangeAction.bitRate)
            }
    ))

    private fun repoMapper(): Observable<Result> = Observable.merge(listOf(
        bitRateRepo.observe()
            .map { bitRate -> Result.BitrateChangedResult(bitRate) },
        sampleRateRepo.observe()
            .map { sampleRate -> Result.SampleRateChangeResult(sampleRate) },
        requestPermissionsRepo.observe()
            .filter { shouldRequestPermissionsOneShot -> !shouldRequestPermissionsOneShot.isShot }
            .map { it.valueOnce }
            .ofType(Denied::class.java)
            .map { denied -> Result.RequestPermissionsResult(denied.permissions) },
        logRepo.observe()
            .ofType(LogRepo.Message::class.java)
            .map { message -> Result.MessageLogResult(message.message) },
        logRepo.observe()
            .ofType(LogRepo.Error::class.java)
            .map { message -> Result.ErrorLogResult(message.error) },
        stateRepo.observe()
            .map { state -> Result.StateChangedResult(state) },
        playerIdRepo.observe()
            .filter { !it.isShot }
            .map { it.valueOnce }
            .map { playerId -> Result.PlayerIdResult(playerId) }
    ))
}
