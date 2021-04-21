package com.omar.retromp3recorder.app.ui.files.edit.delete

import com.omar.retromp3recorder.bl.SetCurrentFileUC
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import javax.inject.Inject

class DeleteFileInteractor @Inject constructor(
    private val deleteFileUC: SetCurrentFileUC,
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<DeleteFileView.Input, DeleteFileView.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<DeleteFileView.Output> = {
        Observable.merge(
            listOf(
            )
        )
    }
    private val mapInputToUsecase: (Observable<DeleteFileView.Input>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                )
            )
        }
}