package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class StopPlaybackAndRecordUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val stopRecordUC: StopRecordUC,
    private val stateMapper: AudioStateMapper,
) {
    fun execute(): Completable = stateMapper
        .observe()
        .take(1)
        .flatMapCompletable { state: AudioState ->
            when (state) {
                AudioState.Playing,
                AudioState.Seek_Paused -> Completable.fromAction { audioPlayer.onInput(AudioPlayer.Input.Stop) }
                AudioState.Recording -> stopRecordUC.execute()
                is AudioState.Idle -> Completable.complete()
            }
        }
}
