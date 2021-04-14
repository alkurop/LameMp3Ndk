package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.utils.FileLister
import com.omar.retromp3recorder.utils.FilePathGenerator
import io.reactivex.Completable
import javax.inject.Inject

class LookForFilesUC @Inject constructor(
    private val fileListRepo: FileListRepo,
    private val filePathGenerator: FilePathGenerator,
    private val fileLister: FileLister
) {
    fun execute(): Completable {
        return Completable.fromAction {
            val fileDir = filePathGenerator.fileDir
            fileListRepo.onNext(fileLister.listFiles(fileDir))
        }
    }
}
