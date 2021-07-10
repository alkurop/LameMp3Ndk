package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.Mp3TagsEditor
import com.omar.retromp3recorder.utils.RecordingTagsDefaultProvider
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class StopRecordUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val mp3TagsEditor: Mp3TagsEditor,
    private val recordingTagsDefaultProvider: RecordingTagsDefaultProvider,
    private val saveRecordingWithWavetableUC: SaveRecordingWithWavetableUC,
    private val scheduler: Scheduler,
    private val voiceRecorder: Mp3VoiceRecorder
) {
    fun execute(): Completable = Completable
        .fromAction {
            voiceRecorder.stopRecord()
        }
        .andThen(saveRecordingWithWavetableUC.execute())
        .andThen(currentFileRepo.observe().takeOne().flatMapCompletable { currentFileWrapper ->
            Completable.merge(
                listOf(
                    Completable.fromAction {
                        val filepath = currentFileWrapper.value!!
                        mp3TagsEditor.setTags(
                            filepath,
                            recordingTagsDefaultProvider.provideDefaults().copy(
                                title = mp3TagsEditor.getFilenameFromPath(filepath)
                            )
                        )
                    },
                    //silly way to update current file preview and show wavetable of recently
                    //recorded file
                    Completable.fromAction {
                        currentFileRepo.onNext(currentFileWrapper)
                    }
                ))
        })
        .subscribeOn(scheduler)
}