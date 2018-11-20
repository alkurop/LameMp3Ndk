package com.omar.retromp3recorder.app.files.usecase

import com.omar.retromp3recorder.app.files.FilePathGenerator
import com.omar.retromp3recorder.app.files.repo.CurrentFileRepo
import io.reactivex.Completable
import javax.inject.Inject

class GenerateNewFilenameForRecorderUC @Inject constructor(
        private val currentFileRepo: CurrentFileRepo,
        private val filePathGenerator: FilePathGenerator
) {
    fun execute(): Completable {
        return Completable.fromAction {
            val filePath = filePathGenerator.generateFilePath()
            currentFileRepo.newValue(filePath)
        }
    }
}