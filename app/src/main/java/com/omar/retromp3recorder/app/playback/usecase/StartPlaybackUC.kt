package com.omar.retromp3recorder.app.playback.usecase

import android.Manifest
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo.ShouldRequestPermissions
import com.omar.retromp3recorder.app.common.usecase.StopPlaybackAndRecordUC
import com.omar.retromp3recorder.app.files.repo.CurrentFileRepo
import com.omar.retromp3recorder.app.playback.player.AudioPlayer
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder
import com.omar.retromp3recorder.app.recording.usecase.CheckPermissionsUC
import io.reactivex.Completable
import javax.inject.Inject

class StartPlaybackUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val audioPlayer: AudioPlayer,
    private val voiceRecorder: VoiceRecorder,
    private val stopUC: StopPlaybackAndRecordUC,
    private val checkPermissionsUC: CheckPermissionsUC,
    private val requestPermissionsRepo: RequestPermissionsRepo
) {
    fun execute(): Completable {
        val begForPermissions = Completable.complete()
        val execute = currentFileRepo.observe()
            .take(1)
            .flatMapCompletable { fileName: String? ->
                Completable
                    .fromAction {
                        voiceRecorder.stopRecord()
                        audioPlayer.playerStart(fileName)
                    }
            }
        val merge = checkPermissionsUC.execute(playbackPermissions)
            .andThen(
                requestPermissionsRepo.observe()
                    .take(1)
                    .share()
            )
            .map { it.checkValue() }
            .flatMapCompletable { shouldAskPermissions: ShouldRequestPermissions? -> if (shouldAskPermissions is ShouldRequestPermissions.Granted) execute else begForPermissions }
        return stopUC.execute()
            .andThen(merge)
    }

    private val playbackPermissions: Set<String> = setOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}