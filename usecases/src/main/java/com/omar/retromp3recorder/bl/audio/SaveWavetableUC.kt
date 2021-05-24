package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.WavetableRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class SaveWavetableUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val wavetableRepo: WavetableRepo
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
                        val waveOwner = fileEntityDao.getByFilepath(path)[0]
                        val update = waveOwner.copy(waveform = wave.toDatabaseEntity())
                        fileEntityDao.updateItem(update)
                    }
                }
            }
}