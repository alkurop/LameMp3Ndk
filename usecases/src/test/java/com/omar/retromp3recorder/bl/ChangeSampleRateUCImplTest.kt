package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.SampleRateRepo
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class ChangeSampleRateUCImplTest {
    @Inject
    lateinit var sampleRateRepo: SampleRateRepo
    private lateinit var changeSampleRateUC: ChangeSampleRateUC
    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
        changeSampleRateUC = ChangeSampleRateUC(sampleRateRepo)
    }

    @Test
    fun `assert happy flow`() {
        changeSampleRateUC.execute(Mp3VoiceRecorder.SampleRate._8000).subscribe()

        //Then
        sampleRateRepo.observe().test().assertValue(
            Mp3VoiceRecorder.SampleRate._8000
        )
    }
}