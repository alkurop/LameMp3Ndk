package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import io.reactivex.Completable
import javax.inject.Inject

class StopPlaybackUC @Inject constructor(
    private val audioPlayer: AudioPlayer
) {
    fun execute(): Completable {
        return Completable
            .fromAction { audioPlayer.playerStop() }
    }
}