package com.omar.retromp3recorder.app.recording.usecase

import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder.SampleRate
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo
import io.reactivex.Completable
import javax.inject.Inject

class ChangeSampleRateUC @Inject constructor(private val repo: SampleRateRepo) {
    fun execute(sampleRate: SampleRate): Completable {
        return Completable.fromAction { repo.newValue(sampleRate) }
    }
}
