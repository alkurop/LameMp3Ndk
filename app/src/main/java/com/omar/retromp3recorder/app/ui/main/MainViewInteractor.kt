package com.omar.retromp3recorder.app.ui.main

import com.omar.retromp3recorder.app.ui.main.MainView.Output
import com.omar.retromp3recorder.bl.ChangeBitrateUC
import com.omar.retromp3recorder.bl.ChangeSampleRateUC
import com.omar.retromp3recorder.state.repos.BitRateRepo
import com.omar.retromp3recorder.state.repos.LogRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo
import com.omar.retromp3recorder.state.repos.SampleRateRepo
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
    private val bitRateRepo: BitRateRepo,
    private val sampleRateRepo: SampleRateRepo,
    private val requestPermissionsRepo: RequestPermissionsRepo,
    private val logRepo: LogRepo,
) {

    fun processIO(): ObservableTransformer<MainView.Input, Output> =
        ObservableTransformer { upstream ->
            upstream.observeOn(scheduler)
                .compose { actions ->
                    Observable.merge(
                        actionsMapper(actions).toObservable(),
                        repoMapper()
                    )
                }
        }

    private fun actionsMapper(actions: Observable<MainView.Input>) = Completable.merge(listOf(
        actions.ofType(MainView.Input.SampleRateChange::class.java)
            .flatMapCompletable { sampleRateChangeAction ->
                changeSampleRateUC.execute(sampleRateChangeAction.sampleRate)
            },
        actions.ofType(MainView.Input.BitRateChange::class.java)
            .flatMapCompletable { bitRateChangeAction ->
                changeBitrateUC.execute(bitRateChangeAction.bitRate)
            }
    ))

    private fun repoMapper(): Observable<Output> = Observable.merge(listOf(
        bitRateRepo.observe()
            .map { bitRate -> Output.BitrateChangedOutput(bitRate) },
        sampleRateRepo.observe()
            .map { sampleRate -> Output.SampleRateChangeOutput(sampleRate) },
        requestPermissionsRepo.observe()
            .ofType(RequestPermissionsRepo.ShouldRequestPermissions.Denied::class.java)
            .map { it.permissions }
            .flatMapGhost()
            .map { denied -> Output.RequestPermissionsOutput(denied) },
        logRepo.observe()
            .ofType(LogRepo.Event.Message::class.java)
            .map { message -> Output.MessageLogOutput(message.message) },
        logRepo.observe()
            .ofType(LogRepo.Event.Error::class.java)
            .map { message -> Output.ErrorLogOutput(message.error) },
    )
    )
}
