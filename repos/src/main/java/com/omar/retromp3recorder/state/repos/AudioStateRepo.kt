package com.omar.retromp3recorder.state.repos

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.FileEmptyChecker
import io.reactivex.Observable
import javax.inject.Inject

class AudioStateRepo @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val fileEmptyChecker: FileEmptyChecker,
    private val player: AudioPlayer,
    private val recorder: Mp3VoiceRecorder
) {

    fun observe(): Observable<AudioState> = Observable.combineLatest(
        player.observeState(),
        recorder.observeState(),
        currentFileRepo.observe(),
        { playerState, recorderState, currentFilePath ->
            val hasSomethingToPlay = fileEmptyChecker.isFileEmpty(currentFilePath.value).not()
            when {
                playerState == AudioPlayer.State.Playing -> AudioState.Playing
                recorderState == Mp3VoiceRecorder.State.Recording -> AudioState.Recording
                else -> AudioState.Idle(hasSomethingToPlay)
            }
        }
    ).distinctUntilChanged()
}

sealed class AudioState {
    data class Idle(val hasFile: Boolean) : AudioState()
    object Playing : AudioState()
    object Recording : AudioState()
}