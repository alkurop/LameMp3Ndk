package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.bl.files.FindFilesUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class StopRecordUC @Inject constructor(
    private val findFilesUC: FindFilesUC,
    private val saveWavetableUC: SaveWavetableUC,
    private val voiceRecorder: Mp3VoiceRecorder,
) {
    fun execute(): Completable = Completable
        .fromAction {
            voiceRecorder.stopRecord()
        }
        .andThen(findFilesUC.execute())
        .andThen(saveWavetableUC.execute())
}