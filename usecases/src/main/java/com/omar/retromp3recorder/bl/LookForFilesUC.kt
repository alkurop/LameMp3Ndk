package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.files.FilePathGenerator
import com.omar.retromp3recorder.state.repos.FileListRepo
import io.reactivex.Completable
import java.io.File
import javax.inject.Inject

class LookForFilesUC @Inject constructor(
    private val fileListRepo: FileListRepo,
    private val filePathGenerator: FilePathGenerator
) {
    fun execute(): Completable {
        return Completable.fromAction {
            println("privet2")
            val fileDir = filePathGenerator.fileDir
            val file = File(fileDir)
            val list = file.listFiles()?.map { it.absolutePath }?.sorted() ?: emptyList()
            fileListRepo.onNext(list)
        }
    }
}
