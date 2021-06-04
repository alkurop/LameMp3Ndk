package com.omar.retromp3recorder.bl.audio

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.PlayerStartOptions
import com.omar.retromp3recorder.audioplayer.toPlayerTime
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.SeekToPositionRepo
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class AudioSeekUC @Inject constructor(
    private val audioPlayer: AudioPlayer,
    private val seekToPositionRepo: SeekToPositionRepo,
    private val currentFileRepo: CurrentFileRepo
) {
    fun execute(position: Int): Completable =
        Completable.fromAction {
            if (audioPlayer.isPlaying) {
                audioPlayer.seek(position.toPlayerTime())
            } else {
                seekToPositionRepo.onNext(Shell(position))
                val blockingFirst = currentFileRepo.observe().blockingFirst()
                audioPlayer.playerStart(
                    PlayerStartOptions(
                        filePath = blockingFirst.value!!,
                        seekPosition = position.toPlayerTime()
                    )
                )
            }
        }
}