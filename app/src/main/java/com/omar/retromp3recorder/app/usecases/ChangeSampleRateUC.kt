package com.omar.retromp3recorder.app.usecases

import com.omar.retromp3recorder.app.modules.recording.Mp3VoiceRecorder.SampleRate
import com.omar.retromp3recorder.app.state.SampleRateRepo
import io.reactivex.Completable
import javax.inject.Inject

class ChangeSampleRateUC @Inject constructor(private val repo: SampleRateRepo) {
    fun execute(sampleRate: SampleRate): Completable {
        return Completable.fromAction { repo.newValue(sampleRate) }
    }
}
