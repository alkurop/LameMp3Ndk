package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PlayerProgressMapper @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val currentFileRepo: CurrentFileRepo,
) {
    fun observe(): Observable<Pair<Long, Long>> =
        audioPlayer.observerProgress()
            .mergeWith(currentFileRepo.observe().map { Pair(0L, 0L) })
}