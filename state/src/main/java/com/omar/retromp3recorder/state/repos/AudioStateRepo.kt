package com.omar.retromp3recorder.state.repos

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import io.reactivex.Observable
import java.io.File
import javax.inject.Inject

class AudioStateRepo @Inject constructor(
    private val recorder: Mp3VoiceRecorder,
    private val player: AudioPlayer,
    private val currentFileRepo: CurrentFileRepo
) {

    fun observe(): Observable<AudioState> = Observable.combineLatest(
        player.observeState(),
        recorder.observeState(),
        currentFileRepo.observe(),
        { playerState, recorderState, currentFilePath ->
            val file = File(currentFilePath)
            val hasSomethingToPlay = file.exists() && file.length() > 0
            when {
                playerState == AudioPlayer.State.Playing -> AudioState.Playing
                recorderState == Mp3VoiceRecorder.State.Recording -> AudioState.Recording
                else -> AudioState.Idle(hasSomethingToPlay)
            }
        }
    ).distinctUntilChanged()
}
