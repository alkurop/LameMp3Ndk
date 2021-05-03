package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Completable
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
