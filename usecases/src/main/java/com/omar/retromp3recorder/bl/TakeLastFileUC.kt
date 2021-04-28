package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.utils.FileEmptyChecker
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
                val lastFile = fileListRepo.observe().blockingFirst().lastOrNull()
                if (lastFile == null || fileEmptyChecker.isFileEmpty(lastFile.path)) {
                    currentFileRepo.onNext(Optional.empty())
                } else {
                    currentFileRepo.onNext(Optional(lastFile.path))
                }
            })
    }
}
