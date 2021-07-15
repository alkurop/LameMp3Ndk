package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.FileDbEntity
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.storage.repo.FileListRepo
import com.omar.retromp3recorder.utils.FileEmptyChecker
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

/**
 * Looks for files that exist in db
 */
class ScanSlowUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val fileListRepo: FileListRepo,
    private val fileEmptyChecker: FileEmptyChecker,
) {
    fun execute(): Completable = Completable
        .fromAction {
            val fileEntityDao = appDatabase.fileEntityDao()
            val fileList = fileEntityDao.getAll()
            val contentList = mutableListOf<FileDbEntity>()
            val noContentList = mutableListOf<FileDbEntity>()
            fileList.forEach {
                if (fileEmptyChecker.isFileEmpty(it.filepath))
                    noContentList.add(it)
                else
                    contentList.add(it)
            }
            fileListRepo.onNext(contentList.map { it.toFileWrapper() })
            fileEntityDao.delete(noContentList)
        }
}
