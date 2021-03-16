package com.omar.retromp3recorder.app.state

import com.omar.retromp3recorder.app.modules.playback.AudioPlayer
import com.omar.retromp3recorder.app.recorder.Mp3VoiceRecorder
import io.reactivex.Observable
import javax.inject.Inject

class AudioStateRepo @Inject constructor(
    private val recorder: Mp3VoiceRecorder,
    private val player: AudioPlayer
) {

    fun observe(): Observable<AudioState> = Observable.combineLatest(
        player.observeState(),
        recorder.observeState(), { playerState, recorderState ->
            when {
                playerState == AudioPlayer.State.Playing -> AudioState.Playing
                recorderState == Mp3VoiceRecorder.State.Recording -> AudioState.Recording
                else -> AudioState.Idle
            }
        }
    ).distinctUntilChanged()
}
