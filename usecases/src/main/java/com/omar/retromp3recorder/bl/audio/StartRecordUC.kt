package com.omar.retromp3recorder.bl.audio

import android.Manifest
import com.omar.retromp3recorder.bl.CheckPermissionsUC
import com.omar.retromp3recorder.bl.files.GenerateDirIfNotExistsUC
import com.omar.retromp3recorder.bl.files.GetNewFileNameUC
import com.omar.retromp3recorder.bl.files.IncrementFileNameUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.BitRateRepo
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.RequestPermissionsRepo
import com.omar.retromp3recorder.storage.repo.RequestPermissionsRepo.ShouldRequestPermissions
import com.omar.retromp3recorder.storage.repo.SampleRateRepo
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function3
import javax.inject.Inject

class StartRecordUC @Inject constructor(
    private val bitRateRepo: BitRateRepo,
    private val checkPermissionsUC: CheckPermissionsUC,
    private val currentFileRepo: CurrentFileRepo,
    private val getNewFileNameUC: GetNewFileNameUC,
    private val generateDirIfNotExistsUC: GenerateDirIfNotExistsUC,
    private val incrementFileNameUC: IncrementFileNameUC,
    private val recordWavetableMapper: RecordWavetableMapper,
    private val requestPermissionsRepo: RequestPermissionsRepo,
    private val sampleRateRepo: SampleRateRepo,
    private val voiceRecorder: Mp3VoiceRecorder
) {
    fun execute(): Completable {
        val propsZipper = Function3 { filepath: String,
                                      bitRate: Mp3VoiceRecorder.BitRate,
                                      sampleRate: Mp3VoiceRecorder.SampleRate ->
            Mp3VoiceRecorder.RecorderProps(filepath, bitRate, sampleRate)
        }
        val execute = generateDirIfNotExistsUC.execute()
            .andThen(
                Observable.zip(
                    getNewFileNameUC.execute().toObservable(),
                    bitRateRepo.observe().takeOne(),
                    sampleRateRepo.observe().takeOne(),
                    propsZipper
                )
            )
            .flatMapCompletable { props: Mp3VoiceRecorder.RecorderProps ->
                Completable.fromAction {
                    currentFileRepo.onNext(Optional(props.filepath))
                    voiceRecorder.record(props)
                }
            }
            .andThen(incrementFileNameUC.execute())
            .andThen(recordWavetableMapper.execute())
        val abort = Completable.complete()
        return checkPermissionsUC
            .execute(voiceRecordPermissions)
            .andThen(requestPermissionsRepo.observe().takeOne())
            .flatMapCompletable { shouldAskPermissions ->
                if (shouldAskPermissions is ShouldRequestPermissions.Granted) execute else abort
            }
    }

    private val voiceRecordPermissions: Set<String> = setOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )
}