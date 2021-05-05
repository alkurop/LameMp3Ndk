package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.utils.FileRenamer
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class RenameFileUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val appDatabase: AppDatabase,
    private val fileRenamer: FileRenamer,
    private val lookForFilesUC: LookForFilesUC,
    private val currentFileMapper: CurrentFileMapper
) {
    fun execute(newFileName: String, finishedCallback: BehaviorSubjectRepo<Boolean>): Completable =
        currentFileMapper.observe()
            .switchMapCompletable { optional ->
                val fileWrapper = (optional.value!! as ExistingFileWrapper)
                Completable
                    .fromAction {
                        val newPath = fileRenamer.renameFile(fileWrapper, newFileName)
                        val copy = fileWrapper.copy(
                            path = newPath,
                            modifiedTimestamp = System.currentTimeMillis()
                        )
                        appDatabase.fileEntityDao().updateItem(copy.toDatabaseEntity())
                        currentFileRepo.onNext(Optional(newPath))
                    }
            }
            .andThen(lookForFilesUC.execute())
            .andThen(Completable.fromAction { finishedCallback.onNext(true) })
}