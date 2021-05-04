package com.omar.retromp3recorder.bl.settings

import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.RecorderPrefsKeys
import com.omar.retromp3recorder.storage.repo.BitRateRepo
import com.omar.retromp3recorder.storage.repo.SampleRateRepo
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class LoadRecorderSettingsUCTest {
    @Inject
    lateinit var bitRateRepo: BitRateRepo

    @Inject
    lateinit var sampleRateRepo: SampleRateRepo

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var usecase: LoadRecorderSettingsUC

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
        whenever(prefs.getInt(RecorderPrefsKeys.SAMPLE_RATE, -1)) doReturn -1
        whenever(prefs.getInt(RecorderPrefsKeys.BIT_RATE, -1)) doReturn -1
    }

    @Test
    fun `when no setting for sample rate do not update`() {
        val test = sampleRateRepo.observe().test()

        usecase.execute().test().assertComplete().assertNoErrors()

        test.assertValueCount(1)
    }

    @Test
    fun `when no setting for bit rate do not update`() {
        val test = bitRateRepo.observe().test()

        usecase.execute().test().assertComplete().assertNoErrors()

        test.assertValueCount(1)
    }

    @Test
    fun `update sample rate repo with setting`() {
        val sampleRate = Mp3VoiceRecorder.SampleRate._8000
        whenever(prefs.getInt(RecorderPrefsKeys.SAMPLE_RATE, -1)) doReturn sampleRate.ordinal
        val test = sampleRateRepo.observe().test()

        usecase.execute().test().assertComplete().assertNoErrors()

        test.assertValueCount(2)
        assertThat(test.values().last()).isEqualTo(sampleRate)
    }

    @Test
    fun `update bit rate repo with setting`() {
        val bitRate = Mp3VoiceRecorder.BitRate._160
        whenever(prefs.getInt(RecorderPrefsKeys.BIT_RATE, -1)) doReturn bitRate.ordinal
        val test = bitRateRepo.observe().test()

        usecase.execute().test().assertComplete().assertNoErrors()

        test.assertValueCount(2)
        assertThat(test.values().last()).isEqualTo(bitRate)
    }
}