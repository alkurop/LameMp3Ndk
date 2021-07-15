package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.utils.FileRenamer
import com.omar.retromp3recorder.utils.Mp3TagsEditor
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class RenameFileUC @Inject constructor(
    private val appDatabase: AppDatabase,
    private val currentFileRepo: CurrentFileRepo,
    private val currentFileMapper: CurrentFileMapper,
    private val fileRenamer: FileRenamer,
    private val findFilesUC: FindFilesNoScanUC,
    private val mp3TagsEditor: Mp3TagsEditor,
    private val scheduler: Scheduler
) {
    fun execute(newFileName: String, finishedCallback: BehaviorSubjectRepo<Boolean>): Completable =
        currentFileMapper.observe()
            .takeOne()
            .flatMapCompletable { optional ->
                val fileWrapper = (optional.value!! as ExistingFileWrapper)
                Completable.concat(
                    listOf(
                        Completable
                            .fromAction {
                                val newPath = fileRenamer.renameFile(fileWrapper, newFileName)
                                val tags = mp3TagsEditor.getTags(newPath)
                                    .copy(title = mp3TagsEditor.getFilenameFromPath(newFileName))
                                mp3TagsEditor.setTags(newPath, tags)
                                val copy = fileWrapper.copy(
                                    path = newPath,
                                    modifiedTimestamp = System.currentTimeMillis()
                                )
                                appDatabase.fileEntityDao().updateItem(copy.toDatabaseEntity())
                                currentFileRepo.onNext(Optional(newPath))
                            },
                        findFilesUC.execute(),
                        Completable.fromAction { finishedCallback.onNext(true) }
                    )
                )
            }
            .subscribeOn(scheduler)
}