package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.FileListRepo
import com.omar.retromp3recorder.storage.repo.WavetableRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SaveWavetableUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val wavetableRepo: WavetableRepo,
    private val fileListRepo: FileListRepo
) {
    fun execute(): Completable =
        wavetableRepo.observe()
            .firstElement()
            .flatMapCompletable { shell ->
                val (path, wave) = shell
                Completable.fromAction {
                    val fileEntityDao = appDatabase.fileEntityDao()
                    val fileList = fileListRepo.observe().blockingFirst()
                    val theFile = fileList.find { it.path == path } ?: error(
                        "File not found with fileList $fileList and path $path and wave $wave"
                    )
                    val waveOwner =
                        theFile.toDatabaseEntity().copy(waveform = wave.toDatabaseEntity())
                    val update = waveOwner.copy(waveform = wave.toDatabaseEntity())
                    fileEntityDao.updateItem(update)
                    //update filelist with the wavetable for first file
                    val size = fileList.size
                    val newItem = fileList[size - 1].copy(wavetable = wave)
                    fileListRepo.onNext(fileList.take(size - 1) + listOf(newItem))
                }
            }
}