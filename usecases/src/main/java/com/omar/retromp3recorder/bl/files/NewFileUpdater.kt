package com.omar.retromp3recorder.bl.audio

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.SeekRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

/**
 * Cleans SeekPositionRepo when current file changes
 */
class NewFileUpdater @Inject constructor(
    private val hasPlayableFileMapper: HasPlayableFileMapper,
    private val ppRepo: PPRepo,
    private val scheduler: Scheduler
) {
    fun execute(): Completable =
        hasPlayableFileMapper.observe()
            .flatMapCompletable {
                Completable
                    .fromAction { ppRepo.onNext(PPRepo.In.Hidden) }
            }
            .subscribeOn(scheduler)
}