package com.omar.retromp3recorder.bl

import android.Manifest
import com.omar.retromp3recorder.files.FilePathGenerator
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.BitRateRepo
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo.ShouldRequestPermissions
import com.omar.retromp3recorder.state.repos.SampleRateRepo
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
    private val stopPlaybackAndRecordUC: StopPlaybackAndRecordUC,
    private val voiceRecorder: Mp3VoiceRecorder,
) {
    fun execute(): Completable {
        val propsZipper =
            Function3 { filepath: String, bitRate: Mp3VoiceRecorder.BitRate, sampleRate: Mp3VoiceRecorder.SampleRate ->
                Mp3VoiceRecorder.RecorderProps(filepath, bitRate, sampleRate)
            }
        val abort = Completable.complete()
        val execute = Observable
            .zip(
                Observable.fromCallable { filePathGenerator.generateFilePath() },
                bitRateRepo.observe().take(1),
                sampleRateRepo.observe().take(1),
                propsZipper
            )
            .flatMapCompletable { props: Mp3VoiceRecorder.RecorderProps ->
                Completable.fromAction {
                    currentFileRepo.onNext(props.filepath)
                    voiceRecorder.record(props)
                }
            }
        val merge = checkPermissionsUC.execute(voiceRecordPermissions)
            .andThen(
                requestPermissionsRepo.observe()
                    .take(1)
                    .share()
            )
            .flatMapCompletable { shouldAskPermissions ->
                if (shouldAskPermissions is ShouldRequestPermissions.Granted) execute else abort
            }
        return stopPlaybackAndRecordUC
            .execute()
            .andThen(merge)
    }

    private val voiceRecordPermissions: Set<String> = setOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )
}