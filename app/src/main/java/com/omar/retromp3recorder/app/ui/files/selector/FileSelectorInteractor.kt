package com.omar.retromp3recorder.app.ui.files.selector


import com.omar.retromp3recorder.utils.processIO
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import javax.inject.Inject

class FileSelectorInteractor @Inject constructor(
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<FileSelectorView.Input, FileSelectorView.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<FileSelectorView.Output> = {
        Observable.merge(
            listOf(

            )
        )
    }

    private val mapInputToUsecase: (Observable<FileSelectorView.Input>) -> Completable =
        { _ ->
            Completable.merge(
                listOf(

                )
            )
        }
}