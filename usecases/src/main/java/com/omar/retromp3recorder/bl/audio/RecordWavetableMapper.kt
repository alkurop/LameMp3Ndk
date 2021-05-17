package com.omar.retromp3recorder.bl.audio

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.Wavetable
import com.omar.retromp3recorder.storage.repo.WavetableRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class RecordWavetableMapper @Inject constructor(
    private val recorder: Mp3VoiceRecorder,
    private val audioStateMapper: AudioStateMapper,
    private val wavetableRepo: WavetableRepo
) {
    fun execute(): Completable {
        return observeRecorder(WAVETABLE_SIZE_MILLIS)
            .buffer(
                audioStateMapper.observe(),
                { input ->
                    if (input is AudioState.Idle) Observable.just(input)
                    Observable.empty<AudioState>()
                }
            )
            .map { it.fold("", { a, b -> "$a $b" }) }
            .switchMapCompletable {
                Completable.fromAction {
                    val ghost = Wavetable(
                        WAVETABLE_SIZE_MILLIS,
                        it
                    )
                    Timber.d(ghost.toString())
                    wavetableRepo.onNext(
                        Shell(
                            ghost
                        )
                    )
                }
            }
    }

    private fun observeRecorder(windowMillis: Long): Observable<Int> {
        return recorder.observeRecorder()
            .map { it.toList().average().toInt() }
            .buffer(windowMillis, TimeUnit.MILLISECONDS)
            .map { it.toList().average().toInt() }
    }
}

private const val WAVETABLE_SIZE_MILLIS = 50L