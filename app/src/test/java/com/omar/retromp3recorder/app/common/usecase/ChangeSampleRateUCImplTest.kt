package com.omar.retromp3recorder.app.common.usecase

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo
import com.omar.retromp3recorder.app.recording.usecase.ChangeSampleRateUC
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
    fun test_UC_Executed() {
        changeSampleRateUC.execute(Mp3VoiceRecorder.SampleRate._8000).subscribe()

        //Then
        sampleRateRepo.observe().test().assertValue(
            Mp3VoiceRecorder.SampleRate._8000
        )
    }
}