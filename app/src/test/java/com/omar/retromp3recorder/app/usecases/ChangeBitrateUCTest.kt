package com.omar.retromp3recorder.app.usecases

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class ChangeBitrateUCTest {

    @Inject
    lateinit var bitRateRepo: com.omar.retromp3recorder.state.BitRateRepo
    private lateinit var changeBitrateUC: ChangeBitrateUC

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        changeBitrateUC = ChangeBitrateUC(bitRateRepo)
    }

    @Test
    fun `assert happy flow`() {
        changeBitrateUC.execute(Mp3VoiceRecorder.BitRate._160).subscribe()

        //Then
        bitRateRepo.observe().test()
            .assertValue(Mp3VoiceRecorder.BitRate._160)
    }
}