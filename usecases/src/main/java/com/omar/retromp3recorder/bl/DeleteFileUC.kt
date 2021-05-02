package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.utils.FileDeleter
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class DeleteFileUC @Inject constructor(
    private val fileDeleter: FileDeleter,
    private val currentFileRepo: CurrentFileRepo,
    private val fileListRepo: FileListRepo
) {
    fun execute(filePath: String): Completable {
        return Completable.fromAction {
            fileDeleter.deleteFile(filePath)
            val newFileList = fileListRepo
                .observe()
                .blockingFirst().filter { it.path != filePath }
            fileListRepo.onNext(newFileList)
            val currentFile = currentFileRepo.observe().blockingFirst().value
            if (currentFile == filePath)
                currentFileRepo.onNext(Optional.empty())
        }
    }
}