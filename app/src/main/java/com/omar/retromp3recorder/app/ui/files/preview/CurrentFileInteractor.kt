package com.omar.retromp3recorder.app.ui.files.preview

import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import java.io.File
import javax.inject.Inject

class CurrentFileInteractor @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val scheduler: Scheduler
) {

    fun processIO(): ObservableTransformer<CurrentFileView.Input, CurrentFileView.Output> =
        scheduler.processIO(
            inputMapper = { Completable.never() },
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<CurrentFileView.Output> = {
        Observable.merge(
            listOf(
                currentFileRepo.observe()
                    .map { filePath -> CurrentFileView.Output.CurrentFileOutput(File(filePath)) },
            )
        )
    }
}