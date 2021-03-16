package com.omar.retromp3recorder.app.usecases

import com.omar.retromp3recorder.app.modules.share.Sharer
import io.reactivex.Completable
import javax.inject.Inject

class ShareUC @Inject constructor(private val sharingModule: Sharer) {
    fun execute(): Completable {
        return sharingModule.share()
    }
}
