package com.omar.retromp3recorder.app.ui.files.delete

import com.omar.retromp3recorder.bl.files.DeleteCurrentFileUC
import com.omar.retromp3recorder.bl.files.ExistingFileMapper
import com.omar.retromp3recorder.utils.mapToUsecase
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class DeleteFileInteractor @Inject constructor(
    private val deleteCurrentFileUC: DeleteCurrentFileUC,
    private val currentFileMapper: ExistingFileMapper,
    private val scheduler: Scheduler
) {
    private val dismissSubject = BehaviorSubject.createDefault(false)

    fun processIO(): ObservableTransformer<DeleteFileView.Input, DeleteFileView.Output> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<DeleteFileView.Output> = {
        Observable.merge(
            listOf(
                currentFileMapper.observe().map {
                    DeleteFileView.Output.CurrentFile(
                        it.value
                    )
                },
                dismissSubject.map { DeleteFileView.Output.ShouldDismiss(it) },
            )
        )
    }
    private val mapInputToUsecase: (Observable<DeleteFileView.Input>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.mapToUsecase<DeleteFileView.Input.DeleteFile> {
                        deleteCurrentFileUC.execute(
                            dismissSubject
                        )
                    }
                )
            )
        }
}


