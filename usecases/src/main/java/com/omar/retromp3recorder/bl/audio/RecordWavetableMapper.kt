package com.omar.retromp3recorder.bl.audio

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.WaveForm
import com.omar.retromp3recorder.storage.repo.WavetableRepo
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.math.absoluteValue

class RecordWavetableMapper @Inject constructor(
    private val recorder: Mp3VoiceRecorder,
    private val audioStateMapper: AudioStateMapper,
    private val wavetableRepo: WavetableRepo,
    private val currentFileRepo: CurrentFileRepo
) {
    fun execute(): Completable {
        return observeRecorder(WAVETABLE_SIZE_MILLIS)
            .takeUntil(
                audioStateMapper.observe()
                    .ofType(AudioState.Idle::class.java)
            )
            .collectInto(mutableListOf<Byte>(), { list, item -> list.add(item) })
            .map { it.toByteArray() }
            .flatMapCompletable {
                Completable.fromAction {
                    val ghost = WaveForm(
                        it
                    )
                    val currentFilePath = currentFileRepo.observe().blockingFirst().value!!
                    val pair = Pair(
                        currentFilePath,
                        ghost
                    )
                    wavetableRepo.onNext(
                        Shell(
                            pair
                        )
                    )
                }
            }
    }

    private fun observeRecorder(windowMillis: Long): Observable<Byte> {
        return recorder.observeRecorder()
            .map { array ->
                array.toList().map { it.toInt().absoluteValue }.max() ?: 0
            }
            .buffer(windowMillis, TimeUnit.MILLISECONDS)
            .map { it.max() ?: 0 }
            .map { it.toByte() }
    }
}

private const val WAVETABLE_SIZE_MILLIS = 100L