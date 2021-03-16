package com.omar.retromp3recorder.bl

import android.Manifest
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.RequestPermissionsRepo.ShouldRequestPermissions
import io.reactivex.Completable
import javax.inject.Inject

class StartPlaybackUC @Inject constructor(
    private val currentFileRepo: com.omar.retromp3recorder.state.CurrentFileRepo,
    private val audioPlayer: AudioPlayer,
    private val voiceRecorder: Mp3VoiceRecorder,
    private val stopUC: StopPlaybackAndRecordUC,
    private val checkPermissionsUC: CheckPermissionsUC,
    private val requestPermissionsRepo: com.omar.retromp3recorder.state.RequestPermissionsRepo
) {
    fun execute(): Completable {
        val abort = Completable.complete()
        val execute = currentFileRepo.observe()
            .take(1)
            .flatMapCompletable { fileName ->
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
            .flatMapCompletable { shouldAskPermissions ->
                if (shouldAskPermissions is ShouldRequestPermissions.Granted) execute
                else abort
            }
        return stopUC.execute()
            .andThen(merge)
    }

    private val playbackPermissions: Set<String> = setOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}