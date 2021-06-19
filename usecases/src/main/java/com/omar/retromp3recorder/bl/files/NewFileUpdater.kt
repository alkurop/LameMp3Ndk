package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.storage.repo.SeekRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

/**
 * Cleans SeekPositionRepo when current file changes
 */
class NewFileUpdater @Inject constructor(
    private val hasPlayableFileMapper: HasPlayableFileMapper,
    private val seekRepo: SeekRepo,
    private val scheduler: Scheduler
) {
    fun execute(): Completable =
        hasPlayableFileMapper.observe()
            .flatMapCompletable {
                Completable
                    .fromAction { seekRepo.onNext(Optional.empty()) }
            }
            .subscribeOn(scheduler)
}