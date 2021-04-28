package com.omar.retromp3recorder.app.ui.recorder_settings.beat_rate

import com.omar.retromp3recorder.bl.ChangeBitrateUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.BitRateRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import javax.inject.Inject

class BitRateSettingsInteractor @Inject constructor(
    private val scheduler: Scheduler,
    private val beatRateRepo: BitRateRepo,
    private val changeBitrateUC: ChangeBitrateUC
) {
    fun processIO(): ObservableTransformer<Mp3VoiceRecorder.BitRate, Mp3VoiceRecorder.BitRate> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<Mp3VoiceRecorder.BitRate> = {
        Observable.merge(
            listOf(
                beatRateRepo.observe()
            )
        )
    }

    private val mapInputToUsecase: (Observable<Mp3VoiceRecorder.BitRate>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.ofType(Mp3VoiceRecorder.BitRate::class.java)
                        .flatMapCompletable { changeBitrateUC.execute(it) }
                )
            )
        }
}