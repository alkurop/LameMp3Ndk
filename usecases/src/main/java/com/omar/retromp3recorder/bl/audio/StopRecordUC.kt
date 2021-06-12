package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.bl.files.FindFilesUC
import com.omar.retromp3recorder.dto.RecordingMetadata
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.Mp3MetadataEditor
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class StopRecordUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val findFilesUC: FindFilesUC,
    private val mp3MetadataEditor: Mp3MetadataEditor,
    private val saveWavetableUC: SaveWavetableUC,
    private val scheduler: Scheduler,
    private val voiceRecorder: Mp3VoiceRecorder
) {
    fun execute(): Completable = Completable
        .fromAction {
            voiceRecorder.stopRecord()
        }
        .andThen(findFilesUC.execute())
        .andThen(saveWavetableUC.execute())
        //silly way to update current file preview and show wavetable of recently
        //recorded file
        .andThen(currentFileRepo.observe().takeOne().flatMapCompletable { currentFileWrapper ->
            Completable
                .fromAction {
                    val filepath = currentFileWrapper.value!!
                    mp3MetadataEditor.setMetadata(
                        filepath, RecordingMetadata(
                            title = filepath.split("/").last().split(".").first(),
                            album = "RetroMp3Recorder",
                            artist = "RetroMp3Recorder",
                            year = 2021
                        )
                    )
                }
                .andThen(
                    Completable.fromAction {
                        currentFileRepo.onNext(currentFileWrapper)
                    }
                )
        })
        .subscribeOn(scheduler)
}