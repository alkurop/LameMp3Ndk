package com.omar.retromp3recorder.app.share

import io.reactivex.Completable
import javax.inject.Inject

class ShareUC @Inject constructor(private val sharingModule: Sharer) {
    fun execute(): Completable {
        return sharingModule.share()
    }
}
