package com.omar.retromp3recorder.app.usecases

import com.omar.retromp3recorder.app.modules.playback.AudioPlayer
import io.reactivex.Completable
import javax.inject.Inject

class StopPlaybackUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val lookForFilesUC: LookForFilesUC
) {
    fun execute(): Completable {
        return Completable
            .fromAction { audioPlayer.playerStop() }
            .andThen(lookForFilesUC.execute())
    }
}