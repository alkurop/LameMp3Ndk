package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.toSeekbarTime
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PlayerProgressMapper @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val currentFileRepo: CurrentFileRepo,
) {
    fun observe(): Observable<Optional<Pair<Int, Int>>> =
        audioPlayer.observerProgress()
            .map { (position, duration) ->
                Optional(Pair(position.toSeekbarTime(), duration.toSeekbarTime()))
            }
            .mergeWith(currentFileRepo.observe()
                .map { Optional.empty() }
            )
}