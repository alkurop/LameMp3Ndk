package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import io.reactivex.Completable
import java.io.File
import javax.inject.Inject

class ShareUC @Inject constructor(
    private val sharingModule: Sharer,
    private val currentFileRepo: CurrentFileRepo
) {
    fun execute(): Completable {
        return currentFileRepo
            .observe()
            .take(1)
            .flatMapCompletable { fileName -> sharingModule.share(File(fileName)) }
    }
}
