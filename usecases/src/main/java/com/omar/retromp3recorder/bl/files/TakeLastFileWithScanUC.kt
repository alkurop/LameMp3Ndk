package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.FileListRepo
import com.omar.retromp3recorder.utils.FileEmptyChecker
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

/**
 * Runs the FindFilesUC
 *
 * Tasks the last file from file list repo
 * (we expect it to be the last recording)
 *
 * and puts it into the CurrentFileRepo
 */
@Deprecated("use TakeLastFileNoScanUC. this version is very slow when user has a lot of files")
class TakeLastFileWithScanUC @Inject constructor(
    private val fileListRepo: FileListRepo,
    private val fileEmptyChecker: FileEmptyChecker,
    private val findFilesUC: FindFilesUC,
    private val currentFileRepo: CurrentFileRepo,
    private val scheduler: Scheduler
) {
    fun execute(): Completable {
        return findFilesUC.execute()
            .andThen(Completable.fromAction {
                val lastFile = fileListRepo.observe().blockingFirst().lastOrNull()
                if (lastFile == null || fileEmptyChecker.isFileEmpty(lastFile.path)) {
                    currentFileRepo.onNext(Optional.empty())
                } else {
                    currentFileRepo.onNext(Optional(lastFile.path))
                }
            })
            .subscribeOn(scheduler)
    }
}
