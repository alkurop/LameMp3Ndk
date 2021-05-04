package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.bl.files.LookForFilesUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateMapper
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class StopPlaybackAndRecordUC @Inject constructor(
    private val voiceRecorder: Mp3VoiceRecorder,
    private val audioPlayer: AudioPlayer,
    private val stateMapper: AudioStateMapper,
    private val lookForFilesUC: LookForFilesUC
) {
    fun execute(): Completable = stateMapper
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
        .andThen(lookForFilesUC.execute())
}