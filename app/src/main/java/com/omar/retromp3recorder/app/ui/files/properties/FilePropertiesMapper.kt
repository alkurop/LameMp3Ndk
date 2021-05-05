package com.omar.retromp3recorder.app.ui.files.properties

import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class FilePropertiesMapper @Inject constructor(
    private val appDatabase: AppDatabase,
    private val currentFileRepo: CurrentFileRepo,
    private val scheduler: Scheduler
) {
    fun observe(): Observable<PropertiesView.Output.CurrentFileProperties> {
        return currentFileRepo.observe()
            .flatMap { currentFile ->
                val filepath = currentFile.value
                if (filepath == null) Observable.just(
                    PropertiesView.Output.CurrentFileProperties(
                        null
                    )
                )
                else Observable.fromCallable {
                    PropertiesView.Output.CurrentFileProperties(
                        appDatabase.fileEntityDao().getByFilepath(filepath).firstOrNull()?.toFileWrapper()
                    )
                }
            }
            .subscribeOn(scheduler)
            .share()
    }
}