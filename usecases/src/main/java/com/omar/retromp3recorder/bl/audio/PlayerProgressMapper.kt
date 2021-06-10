package com.omar.retromp3recorder.bl.audio

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.observeProgress
import com.omar.retromp3recorder.audioplayer.toSeekbarTime
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.SeekRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PlayerProgressMapper @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val currentFileRepo: CurrentFileRepo,
    private val seekRepo: SeekRepo
) {
    fun observe(): Observable<Optional<Pair<Int, Int>>> =
        audioPlayer.observeProgress()
            .map { (position, duration) ->
                val mappedPosition = position.toSeekbarTime()
                val mappedDuration = duration.toSeekbarTime()
                seekRepo.onNext(Shell(if (mappedDuration == mappedPosition) 0 else mappedPosition))
                Optional(Pair(mappedPosition, mappedDuration))
            }
            .mergeWith(currentFileRepo.observe()
                .map { Optional.empty() }
            )
}