package com.omar.retromp3recorder.app.files.usecase

import io.reactivex.Completable
import javax.inject.Inject

class DeleteFIleUC @Inject constructor() {
    fun execute(fileName: String): Completable {
        return Completable.complete()
    }
}