package com.omar.retromp3recorder.bl.audio

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.SeekToPositionRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

/**
 * Cleans SeekPositionRepo when current file changes
 */
class CleanUpSeekRepoUsecase @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val seekToPositionRepo: SeekToPositionRepo,
    private val scheduler: Scheduler
) {
    fun execute(): Completable =
        currentFileRepo.observe()
            .flatMapCompletable {
                Completable
                    .fromAction { seekToPositionRepo.onNext(Shell.empty()) }
            }
            .subscribeOn(scheduler)
}