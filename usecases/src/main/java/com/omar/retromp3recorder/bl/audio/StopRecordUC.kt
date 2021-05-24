package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.bl.files.FindFilesUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class StopRecordUC @Inject constructor(
    private val findFilesUC: FindFilesUC,
    private val saveWavetableUC: SaveWavetableUC,
    private val voiceRecorder: Mp3VoiceRecorder,
    private val currentFileRepo: CurrentFileRepo
) {
    fun execute(): Completable = Completable
        .fromAction {
            voiceRecorder.stopRecord()
        }
        .andThen(findFilesUC.execute())
        .andThen(saveWavetableUC.execute())
        //silly way to update current file preview and show wavetable of recently
        //recorded file
        .andThen(Completable.fromAction {
            val blockingFirst = currentFileRepo.observe().blockingFirst()
            currentFileRepo.onNext(blockingFirst)
        })
}