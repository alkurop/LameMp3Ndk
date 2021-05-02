package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.storage.db.DatabaseI
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.utils.FileLister
import com.omar.retromp3recorder.utils.FilePathGenerator
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class LookForFilesUC @Inject constructor(
    private val database: DatabaseI,
    private val fileListRepo: FileListRepo,
    private val filePathGenerator: FilePathGenerator,
    private val fileLister: FileLister
) {
    fun execute(): Completable {
        return Completable.fromAction {
            val fileDir = filePathGenerator.fileDir
            val dirFiles = fileLister.listFiles(fileDir)
            val dbFiles = database.userDao().getAll().map { it.toFileWrapper() }
            val newFiles = dirFiles.filter { dirFile ->
                dbFiles.map { dbFile -> dbFile.path }.contains(dirFile.path).not()
            }
            database.userDao().insert(newFiles.map { it.toDatabaseEntity() })
            val unresolvedFiles = dbFiles.filter { dbFile ->
                dirFiles.map { it.path }.contains(dbFile.path).not()
            }
            database.userDao().delete(unresolvedFiles.map { it.toDatabaseEntity() })
            fileListRepo.onNext(dirFiles)
        }
    }
}
