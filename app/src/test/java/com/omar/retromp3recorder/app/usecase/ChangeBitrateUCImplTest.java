package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.repo.BitRateRepo;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

public class ChangeBitrateUCImplTest {

    @Inject
    BitRateRepo bitRateRepo;

    private ChangeBitrateUCImpl changeBitrateUC;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        changeBitrateUC = new ChangeBitrateUCImpl(bitRateRepo);
    }

    @Test
    public void test_UC_Executed() {
        changeBitrateUC.execute(VoiceRecorder.BitRate._160).subscribe();

        //Then
        bitRateRepo.observe().test()
                .assertValue(VoiceRecorder.BitRate._160);
    }

}