package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.repo.SampleRateRepo;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

public class ChangeSampleRateUCImplTest {

    @Inject
    SampleRateRepo sampleRateRepo;
    private ChangeSampleRateUCImpl changeSampleRateUC;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        changeSampleRateUC = new ChangeSampleRateUCImpl(sampleRateRepo);
    }

    @Test
    public void test_UC_Executed() {
        changeSampleRateUC.execute(VoiceRecorder.SampleRate._8000).subscribe();

        //Then
        sampleRateRepo.observe().test().assertValue(
          VoiceRecorder.SampleRate._8000
        );
    }

}