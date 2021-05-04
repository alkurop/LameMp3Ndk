package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.RecorderPrefsKeys
import com.omar.retromp3recorder.storage.repo.BitRateRepo
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class ChangeBitrateUCTest {
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var bitRateRepo: BitRateRepo
    private lateinit var changeBitrateUC: ChangeBitrateUC

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
        changeBitrateUC = ChangeBitrateUC(bitRateRepo, sharedPreferences)
    }

    @Test
    fun `assert happy flow`() {
        val bitRate = Mp3VoiceRecorder.BitRate._160
        changeBitrateUC.execute(bitRate).subscribe()
        //Then
        bitRateRepo.observe().test()
            .assertValue(bitRate)

        verify(sharedPreferences.edit()).putInt(RecorderPrefsKeys.BIT_RATE, bitRate.ordinal)
        verify(sharedPreferences.edit()).apply()
        verifyNoMoreInteractions(sharedPreferences.edit())
    }
}