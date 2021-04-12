package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.files.FileEmptyChecker
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.Completable
import javax.inject.Inject

class TakeLastFileUC @Inject constructor(
    private val fileListRepo: FileListRepo,
    private val fileEmptyChecker: FileEmptyChecker,
    private val lookForFilesUC: LookForFilesUC,
    private val currentFileRepo: CurrentFileRepo
) {
    fun execute(): Completable {
        return lookForFilesUC.execute()
            .andThen(Completable.fromAction {
                val currentFilePath = currentFileRepo.observe().blockingFirst().value
                if (currentFilePath != null) return@fromAction
                val lastFilePath = fileListRepo.observe().blockingFirst().lastOrNull()
                if (fileEmptyChecker.isFileEmpty(lastFilePath)) {
                    currentFileRepo.onNext(Optional.empty())
                } else {
                    currentFileRepo.onNext(Optional(lastFilePath))
                }
            })
    }
}
