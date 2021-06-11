package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.utils.DirCreator
import com.omar.retromp3recorder.utils.FilePathGenerator
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class GenerateDirIfNotExistsUC @Inject constructor(
    private val scheduler: Scheduler,
    private val filePathGenerator: FilePathGenerator,
    private val dirCreator: DirCreator
) {
    fun execute(): Completable = Completable
        .fromAction {
            filePathGenerator.fileDirs.forEach {
                dirCreator.createDirIfNotExists(it)
            }
        }
        .subscribeOn(scheduler)
}