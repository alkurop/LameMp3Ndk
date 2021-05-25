package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.storage.repo.FileListRepo
import com.omar.retromp3recorder.utils.FileEmptyChecker
import com.omar.retromp3recorder.utils.FileLister
import com.omar.retromp3recorder.utils.FilePathGenerator
import io.reactivex.rxjava3.core.Completable
import java.io.File
import javax.inject.Inject

/**
 * Looks for files in directory (cache) for files
 *
 * If a file is empty - delete it
 * other files go to the list
 *
 * Then database is synced according to the list
 *
 * Finally FileListRepo is updated
 */
class FindFilesUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val fileListRepo: FileListRepo,
    private val filePathGenerator: FilePathGenerator,
    private val fileEmptyChecker: FileEmptyChecker,
    private val fileLister: FileLister
) {
    fun execute(): Completable = Completable
        .fromAction {
            val fileDir = filePathGenerator.fileDir
            //look for files in dir
            val foundFiles = fileLister.listFiles(fileDir)
            //these are files with content
            val nonEmptyFiles = foundFiles.filter { fileEmptyChecker.isFileEmpty(it.path).not() }
            //and empty files should be deleted
            foundFiles.filter { it !in nonEmptyFiles }.forEach { File(it.path).delete() }
            val dirFiles = nonEmptyFiles.sortedBy { it.createTimedStamp }
            //now get database records
            val dbFiles = appDatabase.fileEntityDao().getAll().map { it.toFileWrapper() }
            val filesToAddToDatabase = dirFiles.filter { dirFile ->
                dbFiles.map { dbFile -> dbFile.path }.contains(dirFile.path).not()
            }

            appDatabase.fileEntityDao().insert(filesToAddToDatabase.map { it.toDatabaseEntity() })
            val recordsToRemoveFromDatabase = dbFiles.filter { dbFile ->
                dirFiles.map { it.path }.contains(dbFile.path).not()
            }

            appDatabase.fileEntityDao()
                .delete(recordsToRemoveFromDatabase.map { it.toDatabaseEntity() })
            //finally update the file list repo
            val updatedList = appDatabase.fileEntityDao().getAll()
            fileListRepo.onNext(updatedList.map { it.toFileWrapper() })
        }
}
