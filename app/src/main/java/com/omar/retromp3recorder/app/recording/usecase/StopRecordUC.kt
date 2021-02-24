package com.omar.retromp3recorder.app.recording.usecase

import com.omar.retromp3recorder.app.common.usecase.NoParamsUseCase
import com.omar.retromp3recorder.app.files.usecase.LookForFilesUC
import com.omar.retromp3recorder.app.playback.player.AudioPlayer
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder
import io.reactivex.Completable
import javax.inject.Inject

class StopRecordUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val voiceRecorder: VoiceRecorder,
    private val lookForFilesUC: LookForFilesUC
) : NoParamsUseCase {
    override fun execute(): Completable = Completable
        .fromAction {
            voiceRecorder.stopRecord()
            audioPlayer.playerStop()
        }
        .andThen(lookForFilesUC.execute())
}
