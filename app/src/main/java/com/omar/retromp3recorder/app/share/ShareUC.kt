package com.omar.retromp3recorder.app.share

import io.reactivex.Completable
import javax.inject.Inject

open class ShareUC @Inject constructor(private val sharingModule: SharingModule) {
    fun execute(): Completable {
        return sharingModule.share()
    }
}
