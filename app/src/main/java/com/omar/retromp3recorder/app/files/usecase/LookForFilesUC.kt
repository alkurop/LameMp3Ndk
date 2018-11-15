package com.omar.retromp3recorder.app.files.usecase

import io.reactivex.Completable
import javax.inject.Inject

class LookForFilesUC @Inject constructor() {

    fun execute(): Completable {
        return Completable.complete()
    }
}
