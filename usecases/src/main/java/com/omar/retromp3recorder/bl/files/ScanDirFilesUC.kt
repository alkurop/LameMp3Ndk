package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.storage.repo.FileListRepo
import com.omar.retromp3recorder.utils.EmptyWavetableGenerator
import com.omar.retromp3recorder.utils.FileEmptyChecker
import com.omar.retromp3recorder.utils.FileLister
import com.omar.retromp3recorder.utils.FilePathGenerator
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
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
class ScanDirFilesUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val emptyWavetableGenerator: EmptyWavetableGenerator,
    private val fileListRepo: FileListRepo,
    private val filePathGenerator: FilePathGenerator,
    private val fileEmptyChecker: FileEmptyChecker,
    private val fileLister: FileLister,
    private val scheduler: Scheduler
) {
    fun execute(
        shouldCheckEmptyFiles: Boolean = false
    ): Completable = Completable
        .fromAction {
            val foundFiles = fileLister.listFiles(filePathGenerator.fileDirs)
                .filter { it.path.split(".").last() == "mp3" }
            val nonEmptyFiles =
                if (shouldCheckEmptyFiles) {
                    foundFiles.filter { fileEmptyChecker.isFileEmpty(it.path).not() }
                        .also { nonEmptyFiles ->
                            foundFiles.filter { it !in nonEmptyFiles }
                                .forEach { File(it.path).delete() }
                        }
                } else {
                    foundFiles
                }
            val dirFiles = nonEmptyFiles.sortedBy { it.createTimedStamp }
            val updatedList = appDatabase.fileEntityDao().run {
                val dbFiles = getAll().map { it.toFileWrapper() }
                val recordsToRemoveFromDatabase = dbFiles.filter { dbFile ->
                    dirFiles.map { it.path }.contains(dbFile.path).not()
                }

                insert(
                    dirFiles
                        .filter { dirFile ->
                        dbFiles.map { dbFile -> dbFile.path }.contains(dirFile.path).not()
                    }
                    .map { it.copy(wavetable = emptyWavetableGenerator.generateWavetable(it.path)) }
                    .map { it.toDatabaseEntity() })

                update(dbFiles.filter { it !in recordsToRemoveFromDatabase }
                    .filter { it.wavetable == null }
                    .map { it.copy(wavetable = emptyWavetableGenerator.generateWavetable(it.path)) }
                    .map { it.toDatabaseEntity() }
                )

                delete(recordsToRemoveFromDatabase.map {
                    it.toDatabaseEntity()
                })

                getAll()
            }
            fileListRepo.onNext(updatedList.map { it.toFileWrapper() })
        }
        .subscribeOn(scheduler)
}
