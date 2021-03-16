package com.omar.retromp3recorder.app.usecases

import com.omar.retromp3recorder.app.modules.recording.Mp3VoiceRecorder.BitRate
import com.omar.retromp3recorder.app.state.BitRateRepo
import io.reactivex.Completable
import javax.inject.Inject

class ChangeBitrateUC @Inject constructor(private val repo: BitRateRepo) {
    fun execute(bitRate: BitRate): Completable {
        return Completable.fromAction { repo.newValue(bitRate) }
    }
}
