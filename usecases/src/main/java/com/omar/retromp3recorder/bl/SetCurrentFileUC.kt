package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.Completable
import javax.inject.Inject

class SetCurrentFileUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo
) {
    fun execute(filePath: String): Completable {
        return Completable.fromAction {
            currentFileRepo.onNext(Optional(filePath))
        }
    }
}