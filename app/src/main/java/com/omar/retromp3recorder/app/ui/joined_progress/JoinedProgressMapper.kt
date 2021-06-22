package com.omar.retromp3recorder.app.ui.joined_progress

import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.bl.audio.RecordWavetableMapper
import com.omar.retromp3recorder.dto.PlayerProgress
import com.omar.retromp3recorder.dto.Wavetable
import com.omar.retromp3recorder.storage.repo.common.PlayerProgressRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class JoinedProgressMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val playerProgressRepo: PlayerProgressRepo,
    private val recorderWavetableMapper: RecordWavetableMapper
) {
    fun observe(): Observable<JoinedProgress> =
        Observable.merge(
            audioStateMapper.observe().ofType(AudioState.Seek_Paused::class.java).switchMap {
                playerProgressRepo.observe().map { JoinedProgress.PlayerProgressShown(it.value!!) }
            },
            audioStateMapper.observe().ofType(AudioState.Playing::class.java).switchMap {
                playerProgressRepo.observe().map { JoinedProgress.PlayerProgressShown(it.value!!) }
            },
            audioStateMapper.observe().ofType(AudioState.Idle::class.java).switchMap {
                playerProgressRepo.observe().map {
                    val progress = it.value
                    if (progress != null)
                        JoinedProgress.PlayerProgressShown(progress)
                    else
                        JoinedProgress.Hidden
                }
            },
            audioStateMapper.observe().ofType(AudioState.Recording::class.java).switchMap {
                recorderWavetableMapper.observe()
                    .takeUntil(audioStateMapper.observe().ofType(AudioState.Idle::class.java))
                    .scan(emptyList<Byte>(), { list, newItem ->
                        list + newItem
                    })
                    .map {
                        JoinedProgress.RecorderProgressShown(
                            it.size * 100L,
                            Wavetable(it.toByteArray(), 100)
                        )
                    }
            }
        )
}

sealed class JoinedProgress {
    object Hidden : JoinedProgress()
    data class RecorderProgressShown(
        val progress: Long,
        val wavetable: Wavetable
    ) : JoinedProgress()

    data class PlayerProgressShown(
        val progress: PlayerProgress
    ) : JoinedProgress()
}
