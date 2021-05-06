package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.RecorderPrefsKeys
import com.omar.retromp3recorder.storage.repo.BitRateRepo
import io.reactivex.rxjava3.core.Completable
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
