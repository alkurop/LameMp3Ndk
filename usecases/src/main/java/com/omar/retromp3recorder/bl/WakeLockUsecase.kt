package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.utils.WakeLockDealer
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class WakeLockUsecase @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val wakeLockDealer: WakeLockDealer
) {
    fun execute(): Completable =
        Completable.merge(
            listOf(
                audioStateMapper.observe().ofType(AudioState.Idle::class.java)
                    .flatMapCompletable {
                        Completable.fromAction {
                            wakeLockDealer.close()
                        }
                    },
                audioStateMapper.observe().ofType(AudioState.Playing::class.java)
                    .flatMapCompletable {
                        Completable.fromAction {
                            wakeLockDealer.open()
                        }
                    },
                audioStateMapper.observe().ofType(AudioState.Recording::class.java)
                    .flatMapCompletable {
                        Completable.fromAction {
                            wakeLockDealer.open()
                        }
                    })
        )
}