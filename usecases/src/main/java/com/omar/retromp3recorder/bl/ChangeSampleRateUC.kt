package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.SampleRateRepo
import io.reactivex.Completable
import javax.inject.Inject

class ChangeSampleRateUC @Inject constructor(
    private val repo: SampleRateRepo
) {
    fun execute(sampleRate: Mp3VoiceRecorder.SampleRate): Completable {
        return Completable.fromAction { repo.onNext(sampleRate) }
    }
}
