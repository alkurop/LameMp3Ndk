package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.repo.SampleRateRepo;

import javax.inject.Inject;

import io.reactivex.Completable;

public class ChangeSampleRateUC {
    private final SampleRateRepo repo;

    @Inject
    public ChangeSampleRateUC(SampleRateRepo repo) {
        this.repo = repo;
    }

    public Completable execute(VoiceRecorder.SampleRate sampleRate) {
        return Completable.fromAction(() -> repo.newValue(sampleRate));
    }
}
