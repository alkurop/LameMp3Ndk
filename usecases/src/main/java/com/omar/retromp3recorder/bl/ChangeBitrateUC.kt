package com.omar.retromp3recorder.bl

import android.content.SharedPreferences
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.BitRateRepo
import com.omar.retromp3recorder.storage.RecorderPrefsKeys
import io.reactivex.Completable
import javax.inject.Inject

class ChangeBitrateUC @Inject constructor(
    private val repo: BitRateRepo,
    private val sharedPreferences: SharedPreferences
) {
    fun execute(bitRate: Mp3VoiceRecorder.BitRate): Completable {
        return Completable.fromAction { repo.onNext(bitRate) }
            .andThen(Completable
                .fromAction {
                    sharedPreferences.edit()
                        .putInt(RecorderPrefsKeys.BIT_RATE, bitRate.ordinal)
                        .apply()
                }
            )
    }
}
