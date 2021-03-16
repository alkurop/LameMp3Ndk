package com.omar.retromp3recorder.app.usecases

import android.Manifest
import com.omar.retromp3recorder.app.state.BitRateRepo
import com.omar.retromp3recorder.app.state.CurrentFileRepo
import com.omar.retromp3recorder.app.state.RequestPermissionsRepo
import com.omar.retromp3recorder.app.state.RequestPermissionsRepo.ShouldRequestPermissions
import com.omar.retromp3recorder.app.state.SampleRateRepo
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function3
import javax.inject.Inject

class StartRecordUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val bitRateRepo: BitRateRepo,
    private val sampleRateRepo: SampleRateRepo,
    private val voiceRecorder: Mp3VoiceRecorder,
    private val stopPlaybackAndRecordUC: StopPlaybackAndRecordUC,
    private val checkPermissionsUC: CheckPermissionsUC,
    private val requestPermissionsRepo: RequestPermissionsRepo,
    private val generateNewFilenameForRecorderUC: GenerateNewFilenameForRecorderUC
) {
    fun execute(): Completable {
        val propsZipper = Function3 { filepath: String, bitRate: Mp3VoiceRecorder.BitRate, sampleRate: Mp3VoiceRecorder.SampleRate ->
            Mp3VoiceRecorder.RecorderProps(filepath, bitRate, sampleRate)
        }
        val abort = Completable.complete()
        val execute = generateNewFilenameForRecorderUC
            .execute()
            .andThen(
                Observable.zip(
                    currentFileRepo.observe().take(1),
                    bitRateRepo.observe().take(1),
                    sampleRateRepo.observe().take(1),
                    propsZipper
                )
            )
            .flatMapCompletable { props: Mp3VoiceRecorder.RecorderProps ->
                Completable
                    .fromAction { voiceRecorder.record(props) }
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