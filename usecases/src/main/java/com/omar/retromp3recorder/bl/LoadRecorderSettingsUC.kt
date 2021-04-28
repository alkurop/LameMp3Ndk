package com.omar.retromp3recorder.bl

import android.content.SharedPreferences
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.BitRateRepo
import com.omar.retromp3recorder.state.repos.SampleRateRepo
import com.omar.retromp3recorder.storage.SharedPrefsRecorderKeys
import io.reactivex.Completable
import io.reactivex.Scheduler
import javax.inject.Inject

class LoadRecorderSettingsUC @Inject constructor(
    private val bitRateRepo: BitRateRepo,
    private val sampleRateRepo: SampleRateRepo,
    private val scheduler: Scheduler,
    private val sharedPreferences: SharedPreferences
) {
    fun execute() = Completable
        .fromAction {
            val noSetting = -1
            sharedPreferences.getInt(SharedPrefsRecorderKeys.SAMPLE_RATE, noSetting)
                .takeIf { it != noSetting }
                ?.let {
                    val sampleRate = Mp3VoiceRecorder.SampleRate.values()[it]
                    sampleRateRepo.onNext(sampleRate)
                }
        }
        .andThen(Completable.fromAction {
            val noSetting = -1
            sharedPreferences.getInt(SharedPrefsRecorderKeys.BIT_RATE, noSetting)
                .takeIf { it != noSetting }
                ?.let {
                    val bitRate = Mp3VoiceRecorder.BitRate.values()[it]
                    bitRateRepo.onNext(bitRate)
                }
        })
        .subscribeOn(scheduler)
}