package com.omar.retromp3recorder.app.files.usecase

import com.omar.retromp3recorder.app.files.repo.CurrentFileRepo
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import javax.inject.Inject

class DeleteFileUC @Inject constructor(
        private val lookForFilesUC: LookForFilesUC,
        private val currentFileRepo: CurrentFileRepo,
        private val selectLastFileUsecase: SelectLastFileUsecase
) {
    fun execute(fileName: String): Completable {
        return Completable
                .fromAction {
                    File(fileName).delete()
                }
                .andThen(lookForFilesUC.execute())
                .andThen(Single
                        .just(currentFileRepo.hasValue())
                )
                .flatMapCompletable { hasValue ->
                    if (hasValue) currentFileRepo
                            .observe()
                            .take(1)
                            .flatMapCompletable { value ->
                                if (value == fileName) {
                                    selectLastFileUsecase.execute()
                                } else Completable.complete()
                            }
                    else Completable.complete()
                }
    }
}