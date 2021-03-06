package com.omar.retromp3recorder.app.recording.usecase

import com.omar.retromp3recorder.app.files.usecase.LookForFilesUC
import com.omar.retromp3recorder.app.playback.player.AudioPlayer
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder
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
