package com.omar.retromp3recorder.app.common.usecase

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder
import com.omar.retromp3recorder.app.recording.repo.BitRateRepo
import com.omar.retromp3recorder.app.recording.usecase.ChangeBitrateUC
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class ChangeBitrateUCTest {

    @Inject
    lateinit var bitRateRepo: BitRateRepo
    private lateinit var changeBitrateUC: ChangeBitrateUC

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        changeBitrateUC = ChangeBitrateUC(bitRateRepo)
    }

    @Test
    fun test_UC_Executed() {
        changeBitrateUC.execute(VoiceRecorder.BitRate._160).subscribe()

        //Then
        bitRateRepo.observe().test()
            .assertValue(VoiceRecorder.BitRate._160)
    }
}