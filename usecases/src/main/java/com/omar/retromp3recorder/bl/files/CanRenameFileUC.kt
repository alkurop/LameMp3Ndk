package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.common.BehaviorSubjectRepo
import com.omar.retromp3recorder.utils.FileRenamer
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CanRenameFileUC @Inject constructor(
    private val fileRenamer: FileRenamer,
    private val currentFileMapper: CurrentFileMapper
) {
    fun execute(newFileName: String, canRenameFileRepo: BehaviorSubjectRepo<Boolean>): Completable =
        currentFileMapper.observe().switchMapCompletable { optional ->
            val fileWrapper = optional.value
            Completable.fromAction {
                canRenameFileRepo.onNext(false)
                val canRename = if (fileWrapper != null && fileWrapper is ExistingFileWrapper)
                    fileRenamer.canRename(fileWrapper, newFileName) else false
                canRenameFileRepo.onNext(canRename)
            }
        }
}