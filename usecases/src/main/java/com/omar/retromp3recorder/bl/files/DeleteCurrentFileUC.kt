package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.FileListRepo
import com.omar.retromp3recorder.utils.FileDeleter
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import javax.inject.Inject

class DeleteCurrentFileUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val currentFileMapper: CurrentFileMapper,
    private val currentFileRepo: CurrentFileRepo,
    private val fileDeleter: FileDeleter,
    private val fileListRepo: FileListRepo
) {
    fun execute(finishedCallback: BehaviorSubject<Boolean>): Completable {
        return currentFileMapper.observe().takeOne().flatMapCompletable { optional ->
            val filePath = (optional.value!! as ExistingFileWrapper).path
            Completable.fromAction {
                fileDeleter.deleteFile(filePath)
                appDatabase.fileEntityDao().deleteByFilepath(filePath)
                val newFileList = fileListRepo
                    .observe()
                    .blockingFirst().filter { it.path != filePath }
                fileListRepo.onNext(newFileList)
                val currentFile = currentFileRepo.observe().blockingFirst().value
                if (currentFile == filePath)
                    currentFileRepo.onNext(Optional.empty())
                finishedCallback.onNext(true)
            }
        }
    }
}