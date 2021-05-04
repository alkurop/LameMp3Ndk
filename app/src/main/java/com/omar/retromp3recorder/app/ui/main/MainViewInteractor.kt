package com.omar.retromp3recorder.app.ui.main

import com.omar.retromp3recorder.app.ui.main.MainView.Output
import com.omar.retromp3recorder.storage.repo.RequestPermissionsRepo
import com.omar.retromp3recorder.utils.flatMapGhost
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class MainViewInteractor @Inject constructor(
    private val scheduler: Scheduler,
    private val requestPermissionsRepo: RequestPermissionsRepo,
) {

    fun processIO(): ObservableTransformer<MainView.Input, Output> =
        scheduler.processIO(
            inputMapper = { Completable.never() },
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> (Observable<Output>) = {
        Observable.merge(listOf(
            requestPermissionsRepo.observe()
                .ofType(RequestPermissionsRepo.ShouldRequestPermissions.Denied::class.java)
                .map { it.permissions }
                .flatMapGhost()
                .map { denied -> Output.RequestPermissionsOutput(denied) },
        )
        )
    }
}
