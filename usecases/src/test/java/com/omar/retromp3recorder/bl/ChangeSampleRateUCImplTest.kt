package com.omar.retromp3recorder.bl

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.SampleRateRepo
import com.omar.retromp3recorder.storage.RecorderPrefsKeys
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class ChangeSampleRateUCImplTest {
    @Inject
    lateinit var sampleRateRepo: SampleRateRepo

    @Inject
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var changeSampleRateUC: ChangeSampleRateUC

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
        changeSampleRateUC = ChangeSampleRateUC(sampleRateRepo, sharedPreferences)
    }

    @Test
    fun `assert happy flow`() {
        val sampleRate = Mp3VoiceRecorder.SampleRate._8000
        changeSampleRateUC.execute(sampleRate).subscribe()
        //Then
        sampleRateRepo.observe().test().assertValue(
            sampleRate
        )

        verify(sharedPreferences.edit()).putInt(
            RecorderPrefsKeys.SAMPLE_RATE,
            sampleRate.ordinal
        )
        verify(sharedPreferences.edit()).apply()
        verifyNoMoreInteractions(sharedPreferences.edit())
    }
}