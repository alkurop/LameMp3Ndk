package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateRepo
import io.reactivex.Completable
import javax.inject.Inject

class StopPlaybackAndRecordUC @Inject constructor(
    private val voiceRecorder: Mp3VoiceRecorder,
    private val audioPlayer: AudioPlayer,
    private val stateRepo: AudioStateRepo
) {
    fun execute(): Completable = stateRepo
        .observe()
        .take(1)
        .flatMapCompletable { state: AudioState ->
            Completable.fromAction {
                when (state) {
                    AudioState.Playing -> audioPlayer.playerStop()
                    AudioState.Recording -> voiceRecorder.stopRecord()
                    is AudioState.Idle -> Unit
                }
            }
        }
}
