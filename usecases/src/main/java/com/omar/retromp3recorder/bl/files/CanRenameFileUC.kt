package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.dto.FileWrapper
import com.omar.retromp3recorder.state.repos.CanRenameFileRepo
import com.omar.retromp3recorder.utils.FileRenamer
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class CanRenameFileUC @Inject constructor(
    private val fileRenamer: FileRenamer,
    private val canRenameFileRepo: CanRenameFileRepo
) {
    fun execute(fileWrapper: FileWrapper, newFileName: String): Completable =
        Completable.fromAction {
            canRenameFileRepo.onNext(Optional.empty())
            val canRename = fileRenamer.canRename(fileWrapper, newFileName)
            canRenameFileRepo.onNext(Optional(canRename))
        }
}