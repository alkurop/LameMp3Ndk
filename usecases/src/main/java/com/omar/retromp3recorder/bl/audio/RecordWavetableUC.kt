package com.omar.retromp3recorder.bl.audio

import com.omar.retromp3recorder.dto.Wavetable
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.WavetableRepo
import com.omar.retromp3recorder.utils.Constants
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

@Suppress("SameParameterValue")
class RecordWavetableUC @Inject constructor(
    private val recorderMapper: RecordWavetableMapper,
    private val audioStateMapper: AudioStateMapper,
    private val wavetableRepo: WavetableRepo,
    private val currentFileRepo: CurrentFileRepo
) {
    fun execute(): Completable {
        return recorderMapper.observe()
            .takeUntil(
                audioStateMapper.observe()
                    .ofType(AudioState.Idle::class.java)
            )
            .collectInto(mutableListOf<Byte>(), { list, item -> list.add(item) })
            .map { it.toByteArray() }
            .flatMapCompletable {
                Completable.fromAction {
                    val ghost = Wavetable(
                        it,
                        Constants.PLAYER_TO_RECORDER_CONVERSION_MILLIS
                    )
                    val currentFilePath = currentFileRepo.observe().blockingFirst().value!!
                    val pair = Pair(
                        currentFilePath,
                        ghost
                    )
                    wavetableRepo.onNext(pair)
                }
            }
    }
}

