package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AudioSeekPauseUC @Inject constructor(
    private val audioPlayer: AudioPlayer
) {
    fun execute(): Completable =
        Completable.fromAction {
            if (audioPlayer.observeState().blockingFirst() == AudioPlayer.State.Playing) {
                audioPlayer.onInput(AudioPlayer.Input.SeekPause)
            }
        }
}