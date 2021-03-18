package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.files.FilePathGenerator
import io.reactivex.Completable
import javax.inject.Inject

class GenerateNewFilenameForRecorderUC @Inject constructor(
    private val currentFileRepo: com.omar.retromp3recorder.state.CurrentFileRepo,
    private val filePathGenerator: FilePathGenerator
) {
    fun execute(): Completable {
        return Completable.fromAction {
            val filePath = filePathGenerator.generateFilePath()
            currentFileRepo.newValue(filePath)
        }
    }
}