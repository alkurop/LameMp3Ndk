package com.omar.retromp3recorder.app.ui.files.edit.rename

import com.omar.retromp3recorder.bl.files.CanRenameFileUC
import com.omar.retromp3recorder.bl.files.RenameFileUC
import com.omar.retromp3recorder.state.repos.CanRenameFileRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class RenameFileInteractor @Inject constructor(
    private val canRenameFileRepo: CanRenameFileRepo,
    private val canRenameFileUC: CanRenameFileUC,
    private val renameFileUC: RenameFileUC,
    private val scheduler: Scheduler
) {
    fun processIO(): ObservableTransformer<RenameFileView.Input, RenameFileView.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<RenameFileView.Output> = {
        Observable.merge(
            listOf(
            )
        )
    }
    private val mapInputToUsecase: (Observable<RenameFileView.Input>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                )
            )
        }
}