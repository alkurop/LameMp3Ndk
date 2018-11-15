package com.omar.retromp3recorder.app.files.usecase

import io.reactivex.Completable
import javax.inject.Inject

class SelectFileUC @Inject constructor() {

    fun execute(filename: String): Completable {
        return Completable.complete()
    }
}
