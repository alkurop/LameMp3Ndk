package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.FileDeleter
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteCurrentFileUC @Inject constructor(
    private val currentFileMapper: CurrentFileMapper,
    private val currentFileRepo: CurrentFileRepo,
    private val fileDeleter: FileDeleter,
    private val takeLastFileUC: TakeLastFileNoScanUC
) {
    fun execute(): Completable {
        return currentFileMapper
            .observe().takeOne()
            .flatMapCompletable { optional ->
                val filePath = (optional.value!! as ExistingFileWrapper).path
                Completable.fromAction {
                    fileDeleter.deleteFile(filePath)
                    val currentFile = currentFileRepo.observe().blockingFirst().value
                    if (currentFile == filePath)
                        currentFileRepo.onNext(Optional.empty())
                }
            }
            .andThen(takeLastFileUC.execute())
    }
}