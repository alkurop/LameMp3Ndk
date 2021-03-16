package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.files_manipulation.FilePathGenerator
import io.reactivex.Completable
import java.io.File
import javax.inject.Inject

class LookForFilesUC @Inject constructor(
    private val fileListRepo: com.omar.retromp3recorder.state.FileListRepo,
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
