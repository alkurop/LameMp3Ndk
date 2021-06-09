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
                val ghost = shell.ghost
                if (ghost == null) Completable.complete()
                else {
                    val (path, wave) = ghost
                    Completable.fromAction {
                        val fileEntityDao = appDatabase.fileEntityDao()
                        val fileList = fileListRepo.observe().blockingFirst()

                        fileList.find { it.path == path }?.let {
                            val waveOwner =
                                it.toDatabaseEntity().copy(waveform = wave.toDatabaseEntity())
                            val update = waveOwner.copy(waveform = wave.toDatabaseEntity())
                            fileEntityDao.updateItem(update)
                            //update filelist with the wavetable for first file
                            val size = fileList.size
                            val newItem = fileList[size - 1].copy(wavetable = wave)
                            fileListRepo.onNext(fileList.take(size - 1) + listOf(newItem))
                        }
                    }
                }
            }
}