package com.omar.retromp3recorder.bl.audio

import android.Manifest
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.bl.CheckPermissionsUC
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo.ShouldRequestPermissions
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class StartPlaybackUC @Inject constructor(
    private val currentFileRepo: CurrentFileRepo,
    private val audioPlayer: AudioPlayer,
    private val checkPermissionsUC: CheckPermissionsUC,
    private val requestPermissionsRepo: RequestPermissionsRepo
) {
    fun execute(): Completable {
        val abort = Completable.complete()
        val execute = currentFileRepo.observe()
            .takeOne()
            .flatMapCompletable { fileName ->
                Completable.fromAction {
                    audioPlayer.playerStart(fileName.value!!)
                }
            }
        return checkPermissionsUC.execute(playbackPermissions)
            .andThen(requestPermissionsRepo.observe().takeOne())
            .flatMapCompletable { shouldAskPermissions ->
                if (shouldAskPermissions is ShouldRequestPermissions.Granted) execute
                else abort
            }
    }

    private val playbackPermissions: Set<String> = setOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
}