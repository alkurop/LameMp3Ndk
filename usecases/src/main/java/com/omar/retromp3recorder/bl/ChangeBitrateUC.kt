package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.BitRateRepo
import io.reactivex.Completable
import javax.inject.Inject

class ChangeBitrateUC @Inject constructor(private val repo: BitRateRepo) {
    fun execute(bitRate: Mp3VoiceRecorder.BitRate): Completable {
        return Completable.fromAction { repo.newValue(bitRate) }
    }
}
