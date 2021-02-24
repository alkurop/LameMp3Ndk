package com.omar.retromp3recorder.app.playback.usecase

import com.omar.retromp3recorder.app.files.usecase.LookForFilesUC
import com.omar.retromp3recorder.app.playback.player.AudioPlayer
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