package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class PlayerIdMapper @Inject internal constructor(
    private val audioPlayer: AudioPlayer
) {
    fun observe(): Observable<Int> {
        return audioPlayer
            .observeEvents()
            .ofType(AudioPlayer.Event.AudioSessionId::class.java)
            .map { it.playerId }.share()
    }
}
