package com.omar.retromp3recorder.app.files.usecase

import com.omar.retromp3recorder.app.files.repo.CurrentFileRepo
import com.omar.retromp3recorder.app.files.repo.FileListRepo
import io.reactivex.Completable
import javax.inject.Inject

class SelectLastFileUsecase @Inject constructor(
        private val fileListRepo: FileListRepo,
        private val currentFileRepo: CurrentFileRepo

) {
    fun execute(): Completable {
        if (fileListRepo.hasValue().not()) {
            return Completable.complete()
        }
        return fileListRepo
                .observe()
                .take(1)
                .flatMapCompletable { items ->
                    if (items.isEmpty()) {
                        Completable.complete()
                    } else {
                        Completable.fromAction {
                            currentFileRepo.newValue(items.last())
                        }
                    }
                }
    }
}