package com.omar.retromp3recorder.app.files.usecase

import com.omar.retromp3recorder.app.files.repo.CurrentFileRepo
import io.reactivex.Completable
import javax.inject.Inject

class SelectFileUC @Inject constructor(
        private val currentFileRepo: CurrentFileRepo
) {

    fun execute(filename: String): Completable {
        return Completable.fromAction {
            currentFileRepo.newValue(filename)
        }
    }
}
