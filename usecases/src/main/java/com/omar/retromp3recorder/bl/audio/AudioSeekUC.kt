package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.toPlayerTime
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AudioSeekUC @Inject constructor(
    private val audioPlayer: AudioPlayer
) {
    fun execute(seekToPosition: Int): Completable =
        Completable.fromAction { audioPlayer.seek(seekToPosition.toPlayerTime()) }
}