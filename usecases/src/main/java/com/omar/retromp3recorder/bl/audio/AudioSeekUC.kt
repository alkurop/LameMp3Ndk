package com.omar.retromp3recorder.bl.audio

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.toPlayerTime
import com.omar.retromp3recorder.storage.repo.SeekToPositionRepo
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AudioSeekUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val seekToPositionRepo: SeekToPositionRepo
) {
    fun execute(position: Int): Completable =
        audioPlayer.observeState().takeOne().flatMapCompletable { state ->
            Completable.fromAction {
                when (state) {
                    AudioPlayer.State.Playing -> {
                        error("Should not be seeking during playback")
                    }
                    AudioPlayer.State.Seek_Paused -> {
                        audioPlayer.onInput(AudioPlayer.Input.Seek(position.toPlayerTime()))
                    }
                    else -> {
                        seekToPositionRepo.onNext(Shell(position))
                    }
                }
            }
        }
}