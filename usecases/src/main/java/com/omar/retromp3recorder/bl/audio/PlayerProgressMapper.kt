package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.observeProgress
import com.omar.retromp3recorder.storage.repo.common.PPRepo
import com.omar.retromp3recorder.utils.toSeekbarTime
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class PlayerProgressMapper @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val ppRepo: PPRepo
) {
    fun execute(): Completable =
        audioPlayer.observeProgress()
            .distinctUntilChanged()
            .flatMapCompletable { (position, duration) ->
                Completable.fromAction {
                    val mappedPosition = position.toSeekbarTime()
                    val mappedDuration = duration.toSeekbarTime()
                    val fixedPosition = if (mappedDuration == mappedPosition) 0 else position
                    ppRepo.onNext(PPRepo.In.Progress(fixedPosition, duration))
                }
            }
}