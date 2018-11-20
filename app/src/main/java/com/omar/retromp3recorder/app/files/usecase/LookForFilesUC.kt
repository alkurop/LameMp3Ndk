package com.omar.retromp3recorder.app.files.usecase

import com.omar.retromp3recorder.app.files.FilePathGenerator
import com.omar.retromp3recorder.app.files.repo.FileListRepo
import io.reactivex.Completable
import java.io.File
import javax.inject.Inject

class LookForFilesUC @Inject constructor(
        private val fileListRepo: FileListRepo,
        private val filePathGenerator: FilePathGenerator
) {
    fun execute(): Completable {
        return Completable.fromAction {
            val fileDir = filePathGenerator.fileDir
            val file = File(fileDir)
            val list = file.listFiles().map { it.absolutePath }.sorted()
            fileListRepo.newValue(list)
        }
    }
}
