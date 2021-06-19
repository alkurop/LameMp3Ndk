package com.omar.retromp3recorder.bl.files

import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.common.PlayerProgressRepo
import com.omar.retromp3recorder.utils.AudioDurationRetriever
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

/**
 * Cleans SeekPositionRepo when current file changes
 */
class NewFileUpdater @Inject constructor(
    private val audioDurationRetriever: AudioDurationRetriever,
    private val currentFileRepo: CurrentFileRepo,
    private val hasPlayableFileMapper: HasPlayableFileMapper,
    private val playerProgressRepo: PlayerProgressRepo,
    private val scheduler: Scheduler
) {
    fun execute(): Completable =
        Observable.zip(
            hasPlayableFileMapper.observe(),
            currentFileRepo.observe(), { hasPlayableFile, currentFile ->
                if (hasPlayableFile) {
                    val durationMillis =
                        audioDurationRetriever.getAudioDurationForExistingFile(currentFile.value!!)
                    PlayerProgressRepo.In.Progress(0, durationMillis)
                } else PlayerProgressRepo.In.Hidden
            })
            .flatMapCompletable { progress ->
                Completable.fromAction {
                    playerProgressRepo.onNext(progress)
                }
            }
            .subscribeOn(scheduler)
}