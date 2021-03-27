package com.omar.retromp3recorder.app.ui.main

import com.omar.retromp3recorder.app.ui.main.MainView.Output
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo
import com.omar.retromp3recorder.utils.flatMapGhost
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
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
