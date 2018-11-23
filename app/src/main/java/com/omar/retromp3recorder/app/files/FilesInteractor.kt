package com.omar.retromp3recorder.app.files

import com.omar.retromp3recorder.app.files.repo.CurrentFileRepo
import com.omar.retromp3recorder.app.files.repo.FileListRepo
import com.omar.retromp3recorder.app.files.usecase.DeleteFileUC
import com.omar.retromp3recorder.app.files.usecase.LookForFilesUC
import com.omar.retromp3recorder.app.files.usecase.SelectFileUC
import com.omar.retromp3recorder.app.files.usecase.SelectLastFileUsecase
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import javax.inject.Inject

class FilesInteractor @Inject constructor(
        private val scheduler: Scheduler,
        private val deleteFileUC: DeleteFileUC,
        private val selectFileUC: SelectFileUC,
        private val lookForFilesUC: LookForFilesUC,
        private val currentFileRepo: CurrentFileRepo,
        private val fileListRepo: FileListRepo,
        private val selectLastFileUsecase: SelectLastFileUsecase
) {
    fun process() = ObservableTransformer<FilesAction, FilesResult> { upstream ->
        upstream
                .observeOn(scheduler)
                .publish { shared ->
                    Observable.merge<FilesResult>(
                            listOf(
                                    shared.ofType(FilesAction.DeleteFileAction::class.java)
                                            .flatMapCompletable {
                                                deleteFileUC.execute(it.filename)
                                            }
                                            .toObservable<FilesResult>(),
                                    shared.ofType(FilesAction.SelectFileAction::class.java)
                                            .flatMapCompletable {
                                                selectFileUC.execute(it.fileName)
                                            }
                                            .toObservable<FilesResult>(),
                                    shared.ofType(FilesAction.InitAction::class.java)
                                            .flatMapCompletable {
                                                lookForFilesUC.execute()
                                                        .andThen(selectLastFileUsecase.execute())
                                            }
                                            .toObservable<FilesResult>(),
                                    currentFileRepo.observe()
                                            .map { FilesResult.FileSelectedResult(it) },
                                    fileListRepo.observe()
                                            .map {FilesResult.FilesListResult(it)}
                            )
                    )
                }
    }
}