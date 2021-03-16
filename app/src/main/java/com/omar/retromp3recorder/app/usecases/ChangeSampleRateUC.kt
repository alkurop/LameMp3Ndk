package com.omar.retromp3recorder.app.usecases

import com.omar.retromp3recorder.app.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.app.state.SampleRateRepo
import io.reactivex.Completable
import javax.inject.Inject

class ChangeSampleRateUC @Inject constructor(private val repo: SampleRateRepo) {
    fun execute(sampleRate: Mp3VoiceRecorder.SampleRate): Completable {
        return Completable.fromAction { repo.newValue(sampleRate) }
    }
}
