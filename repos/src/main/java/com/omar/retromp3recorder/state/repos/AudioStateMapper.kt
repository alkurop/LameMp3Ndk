package com.omar.retromp3recorder.state.repos

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import io.reactivex.Observable
import javax.inject.Inject

interface AudioStateMapper {
    fun observe(): Observable<AudioState>
}

class AudioStateMapperImpl @Inject constructor(
    private val player: AudioPlayer,
    private val recorder: Mp3VoiceRecorder
) : AudioStateMapper {
    override fun observe(): Observable<AudioState> = Observable.combineLatest(
        player.observeState(),
        recorder.observeState(),
        { playerState, recorderState ->
            when {
                playerState == AudioPlayer.State.Playing -> AudioState.Playing
                recorderState == Mp3VoiceRecorder.State.Recording -> AudioState.Recording
                else -> AudioState.Idle
            }
        }
    ).distinctUntilChanged()
}

sealed class AudioState {
    object Idle : AudioState()
    object Playing : AudioState()
    object Recording : AudioState()
}