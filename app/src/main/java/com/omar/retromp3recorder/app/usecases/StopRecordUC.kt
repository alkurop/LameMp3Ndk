package com.omar.retromp3recorder.app.usecases

import com.omar.retromp3recorder.app.modules.playback.AudioPlayer
import com.omar.retromp3recorder.app.recorder.Mp3VoiceRecorder
import io.reactivex.Completable
import javax.inject.Inject

class StopRecordUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val voiceRecorder: Mp3VoiceRecorder,
    private val lookForFilesUC: LookForFilesUC
) {
    fun execute(): Completable = Completable
        .fromAction {
            voiceRecorder.stopRecord()
            audioPlayer.playerStop()
        }
        .andThen(lookForFilesUC.execute())
}
