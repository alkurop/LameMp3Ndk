package com.omar.retromp3recorder.app.usecases

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.state.SampleRateRepo
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class ChangeSampleRateUCImplTest {
    @Inject
    lateinit var sampleRateRepo: SampleRateRepo
    private lateinit var changeSampleRateUC: ChangeSampleRateUC
    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
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