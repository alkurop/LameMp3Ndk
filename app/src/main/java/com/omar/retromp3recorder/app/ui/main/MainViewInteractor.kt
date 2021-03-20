package com.omar.retromp3recorder.app.ui.main

import com.omar.retromp3recorder.app.ui.main.MainView.Result
import com.omar.retromp3recorder.bl.*
import com.omar.retromp3recorder.state.repos.*
import com.omar.retromp3recorder.utils.flatMapGhost
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
    private val stateRepo: AudioStateRepo,
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
            .ofType(RequestPermissionsRepo.ShouldRequestPermissions.Denied::class.java)
            .map { it.permissions }
            .flatMapGhost()
            .map { denied -> Result.RequestPermissionsResult(denied) },
        logRepo.observe()
            .ofType(LogRepo.Event.Message::class.java)
            .map { message -> Result.MessageLogResult(message.message) },
        logRepo.observe()
            .ofType(LogRepo.Event.Error::class.java)
            .map { message -> Result.ErrorLogResult(message.error) },
        stateRepo.observe()
            .map { state -> Result.StateChangedResult(state.map()) },
        playerIdRepo.observe()
            .map { playerId -> Result.PlayerIdResult(playerId) }
    ))
}
