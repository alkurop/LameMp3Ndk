package com.omar.retromp3recorder.app.usecases

import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import io.reactivex.Completable
import javax.inject.Inject

class ChangeSampleRateUC @Inject constructor(private val repo: com.omar.retromp3recorder.state.SampleRateRepo) {
    fun execute(sampleRate: Mp3VoiceRecorder.SampleRate): Completable {
        return Completable.fromAction { repo.newValue(sampleRate) }
    }
}
