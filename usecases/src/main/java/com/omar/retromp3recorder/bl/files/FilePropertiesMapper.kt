package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.FileWrapper
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FilePropertiesMapper @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val appDatabase: AppDatabase
) {
    fun observe(): Observable<Optional<FileWrapper>> {
        return currentFileRepo.observe().switchMap { currentFile ->
            val filepath = currentFile.value
            if (filepath == null) Observable.just(Optional.empty())
            else Observable.fromCallable {
                Optional(appDatabase.fileEntityDao().getByFilepath(filepath)[0].toFileWrapper())
            }
        }
    }
}