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
import kotlin.math.absoluteValue

class RecordWavetableMapper @Inject constructor(
    private val recorder: Mp3VoiceRecorder,
    private val audioStateMapper: AudioStateMapper,
    private val wavetableRepo: WavetableRepo
) {
    fun execute(): Completable {
        return observeRecorder(WAVETABLE_SIZE_MILLIS)
            .takeUntil(
                audioStateMapper.observe()
                    .ofType(AudioState.Idle::class.java)
            )
            .map { it.toString() }
            .collectInto(mutableListOf<String>(), { list, item -> list.add(item) })
            .flatMapCompletable {
                Completable.fromAction {
                    val ghost = Wavetable(
                        WAVETABLE_SIZE_MILLIS,
                        it.joinToString(" ")
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
            .map { array ->
                array.toList().map { it.toInt().absoluteValue }.max() ?: 0
            }
            .buffer(windowMillis, TimeUnit.MILLISECONDS)
            .map { it.max() ?: 0 }
    }
}

private const val WAVETABLE_SIZE_MILLIS = 100L