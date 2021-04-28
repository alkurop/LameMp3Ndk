package com.omar.retromp3recorder.bl

import android.content.SharedPreferences
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.SampleRateRepo
import com.omar.retromp3recorder.storage.RecorderPrefsKeys
import io.reactivex.Completable
import javax.inject.Inject

class ChangeSampleRateUC @Inject constructor(
    private val repo: SampleRateRepo,
    private val sharedPreferences: SharedPreferences
) {
    fun execute(sampleRate: Mp3VoiceRecorder.SampleRate): Completable {
        return Completable.fromAction { repo.onNext(sampleRate) }
            .andThen(Completable.fromAction {
                sharedPreferences.edit()
                    .putInt(RecorderPrefsKeys.SAMPLE_RATE, sampleRate.ordinal)
                    .apply()
            })
    }
}
