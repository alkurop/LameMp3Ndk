package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.BitRateRepo
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class ChangeBitrateUCTest {

    @Inject
    lateinit var bitRateRepo: BitRateRepo
    private lateinit var changeBitrateUC: ChangeBitrateUC

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
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