package com.omar.retromp3recorder.app.usecases

import com.omar.retromp3recorder.app.modules.files.FilePathGenerator
import com.omar.retromp3recorder.app.state.FileListRepo
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
            val list = file.listFiles()?.map { it.absolutePath }?.sorted()?: emptyList()
            fileListRepo.newValue(list)
        }
    }
}
