package com.omar.retromp3recorder.app.recording.usecase

import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.BitRate
import com.omar.retromp3recorder.app.recording.repo.BitRateRepo
import io.reactivex.Completable
import javax.inject.Inject

class ChangeBitrateUC @Inject constructor(private val repo: BitRateRepo) {
    fun execute(bitRate: BitRate): Completable {
        return Completable.fromAction { repo.newValue(bitRate) }
    }
}
