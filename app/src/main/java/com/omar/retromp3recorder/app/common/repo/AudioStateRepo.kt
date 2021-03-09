package com.omar.retromp3recorder.app.common.repo

import com.omar.retromp3recorder.app.playback.player.StatefulAudioPlayer
import com.omar.retromp3recorder.app.recording.recorder.StatefulVoiceRecorder
import io.reactivex.Observable
import javax.inject.Inject

class AudioStateRepo @Inject constructor(
    private val recorder: StatefulVoiceRecorder,
    private val player: StatefulAudioPlayer
) {

    fun observe(): Observable<AudioState> = Observable.combineLatest(
        player.observeState(),
        recorder.observeState(), { playerState, recorderState ->
            when {
                playerState == StatefulAudioPlayer.State.Playing -> AudioState.Playing
                recorderState == StatefulVoiceRecorder.State.Recording -> AudioState.Recording
                else -> AudioState.Idle
            }
        }
    ).distinctUntilChanged()
}
