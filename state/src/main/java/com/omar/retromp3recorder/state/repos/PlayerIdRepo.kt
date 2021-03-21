package com.omar.retromp3recorder.state.repos

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import io.reactivex.Observable
import javax.inject.Inject

class PlayerIdRepo @Inject internal constructor(
    private val audioPlayer: AudioPlayer
) {
    fun observe(): Observable<Int> {
        return audioPlayer
            .observeEvents()
            .ofType(AudioPlayer.Event.PlayerId::class.java)
            .map { it.playerId }
    }
}
