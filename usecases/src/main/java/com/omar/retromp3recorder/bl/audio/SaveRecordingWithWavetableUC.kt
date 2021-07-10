package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.FileListRepo
import com.omar.retromp3recorder.storage.repo.WavetableRepo
import com.omar.retromp3recorder.utils.FileLister
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SaveRecordingWithWavetableUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val wavetableRepo: WavetableRepo,
    private val fileListRepo: FileListRepo,
    private val fileLister: FileLister
) {
    fun execute(): Completable =
        wavetableRepo.observe()
            .takeOne()
            .flatMapCompletable { shell ->
                val (path, wave) = shell
                Completable.fromAction {
                    val fileEntityDao = appDatabase.fileEntityDao()
                    val newItem = fileLister.discoverFile(path).copy(wavetable = wave)

                    fileEntityDao.insert(listOf(newItem.toDatabaseEntity()))
                    val fileList = fileListRepo.observe().blockingFirst()
                    //update filelist with the wavetable for first file
                    fileListRepo.onNext(fileList + listOf(newItem))
                }
            }
}