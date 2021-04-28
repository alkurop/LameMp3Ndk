package com.omar.retromp3recorder.bl

import android.content.SharedPreferences
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.state.repos.BitRateRepo
import com.omar.retromp3recorder.state.repos.SampleRateRepo
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
    }

    @Test
    fun `when no setting for sample rate do not update`() {
    }

    @Test
    fun `when no setting for bit rate do not update`() {
    }

    @Test
    fun `update sample rate repo with setting`() {
    }

    @Test
    fun `update bit rate repo with setting`() {
    }
}