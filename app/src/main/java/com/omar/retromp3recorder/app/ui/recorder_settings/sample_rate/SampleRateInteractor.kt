package com.omar.retromp3recorder.app.ui.recorder_settings.sample_rate

import com.omar.retromp3recorder.bl.settings.ChangeSampleRateUC
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.SampleRateRepo
import com.omar.retromp3recorder.utils.processIO
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class SampleRateInteractor @Inject constructor(
    private val changeSampleRateUC: ChangeSampleRateUC,
    private val scheduler: Scheduler,
    private val sampleRateRepo: SampleRateRepo
) {
    fun processIO(): ObservableTransformer<Mp3VoiceRecorder.SampleRate, Mp3VoiceRecorder.SampleRate> =
        scheduler.processIO(
            inputMapper = mapInputToUsecase,
            outputMapper = mapRepoToOutput
        )

    private val mapRepoToOutput: () -> Observable<Mp3VoiceRecorder.SampleRate> = {
        Observable.merge(
            listOf(
                sampleRateRepo.observe()
            )
        )
    }

    private val mapInputToUsecase: (Observable<Mp3VoiceRecorder.SampleRate>) -> Completable =
        { input ->
            Completable.merge(
                listOf(
                    input.ofType(Mp3VoiceRecorder.SampleRate::class.java)
                        .flatMapCompletable { changeSampleRateUC.execute(it) }
                )
            )
        }
}