package com.omar.retromp3recorder.app.recording.usecase;

import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo;

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
