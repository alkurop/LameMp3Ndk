package com.omar.retromp3recorder.app.ui.main

import com.omar.retromp3recorder.app.ui.main.MainView.Output
import com.omar.retromp3recorder.bl.ChangeBitrateUC
import com.omar.retromp3recorder.bl.ChangeSampleRateUC
import com.omar.retromp3recorder.state.repos.BitRateRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo
import com.omar.retromp3recorder.state.repos.SampleRateRepo
import com.omar.retromp3recorder.utils.flatMapGhost
import com.omar.retromp3recorder.utils.processIO
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
) {

    fun processIO(): ObservableTransformer<MainView.Input, Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapInputToUsecase: (Observable<MainView.Input>) -> Completable = { input ->
        Completable.merge(listOf(
            input.ofType(MainView.Input.SampleRateChange::class.java)
                .flatMapCompletable { sampleRateChangeAction ->
                    changeSampleRateUC.execute(sampleRateChangeAction.sampleRate)
                },
            input.ofType(MainView.Input.BitRateChange::class.java)
                .flatMapCompletable { bitRateChangeAction ->
                    changeBitrateUC.execute(bitRateChangeAction.bitRate)
                }
        ))
    }

    private val mapRepoToOutput :()-> (Observable<Output>) = {
        Observable.merge(listOf(
            bitRateRepo.observe()
                .map { bitRate -> Output.BitrateChangedOutput(bitRate) },
            sampleRateRepo.observe()
                .map { sampleRate -> Output.SampleRateChangeOutput(sampleRate) },
            requestPermissionsRepo.observe()
                .ofType(RequestPermissionsRepo.ShouldRequestPermissions.Denied::class.java)
                .map { it.permissions }
                .flatMapGhost()
                .map { denied -> Output.RequestPermissionsOutput(denied) },
        )
        )
    }
}
