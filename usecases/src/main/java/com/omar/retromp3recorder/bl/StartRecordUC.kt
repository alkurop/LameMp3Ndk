package com.omar.retromp3recorder.bl

import android.Manifest
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.BitRateRepo
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo.ShouldRequestPermissions
import com.omar.retromp3recorder.state.repos.SampleRateRepo
import com.omar.retromp3recorder.utils.FilePathGenerator
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function3
import javax.inject.Inject

class StartRecordUC @Inject constructor(
    private val bitRateRepo: BitRateRepo,
    private val checkPermissionsUC: CheckPermissionsUC,
    private val currentFileRepo: CurrentFileRepo,
    private val filePathGenerator: FilePathGenerator,
    private val requestPermissionsRepo: RequestPermissionsRepo,
    private val sampleRateRepo: SampleRateRepo,
    private val voiceRecorder: Mp3VoiceRecorder,
) {
    fun execute(): Completable {
        val propsZipper = Function3 { filepath: String,
                                      bitRate: Mp3VoiceRecorder.BitRate,
                                      sampleRate: Mp3VoiceRecorder.SampleRate ->
            Mp3VoiceRecorder.RecorderProps(filepath, bitRate, sampleRate)
        }
        val abort = Completable.complete()
        val execute = Observable
            .zip(
                Observable.fromCallable { filePathGenerator.generateFilePath() },
                bitRateRepo.observe().takeOne(),
                sampleRateRepo.observe().takeOne(),
                propsZipper
            )
            .flatMapCompletable { props: Mp3VoiceRecorder.RecorderProps ->
                Completable.fromAction {
                    currentFileRepo.onNext(Optional(props.filepath))
                    voiceRecorder.record(props)
                }
            }
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