package com.omar.retromp3recorder.state.repos

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.fileIfExistsAndNotEmpty
import io.reactivex.Observable
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
            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            val file = currentFilePath.value.fileIfExistsAndNotEmpty()
            val hasSomethingToPlay = file != null
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