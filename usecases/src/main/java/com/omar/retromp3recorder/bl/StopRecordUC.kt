package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import io.reactivex.Completable
import javax.inject.Inject

class StopRecordUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val voiceRecorder: Mp3VoiceRecorder
) {
    fun execute(): Completable = Completable
        .fromAction {
            voiceRecorder.stopRecord()
            audioPlayer.playerStop()
        }
}
