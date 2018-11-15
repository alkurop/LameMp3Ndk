package com.omar.retromp3recorder.app.shared.usecase;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo;
import com.omar.retromp3recorder.app.recording.usecase.ChangeSampleRateUC;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

public class ChangeSampleRateUCImplTest {

    @Inject
    SampleRateRepo sampleRateRepo;
    private ChangeSampleRateUC changeSampleRateUC;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        changeSampleRateUC = new ChangeSampleRateUC(sampleRateRepo);
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