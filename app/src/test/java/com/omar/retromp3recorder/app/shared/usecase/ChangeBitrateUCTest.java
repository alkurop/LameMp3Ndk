package com.omar.retromp3recorder.app.shared.usecase;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.recording.repo.BitRateRepo;
import com.omar.retromp3recorder.app.recording.usecase.ChangeBitrateUC;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

public class ChangeBitrateUCTest {

    @Inject
    BitRateRepo bitRateRepo;

    private ChangeBitrateUC changeBitrateUC;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        changeBitrateUC = new ChangeBitrateUC(bitRateRepo);
    }

    @Test
    public void test_UC_Executed() {
        changeBitrateUC.execute(VoiceRecorder.BitRate._160).subscribe();

        //Then
        bitRateRepo.observe().test()
                .assertValue(VoiceRecorder.BitRate._160);
    }

}