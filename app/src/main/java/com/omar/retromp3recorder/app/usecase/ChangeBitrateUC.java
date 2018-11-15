package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.repo.BitRateRepo;

import javax.inject.Inject;

import io.reactivex.Completable;

public class ChangeBitrateUC {
    private final BitRateRepo repo;

    @Inject
    public ChangeBitrateUC(BitRateRepo repo) {
        this.repo = repo;
    }

    public Completable execute(VoiceRecorder.BitRate bitRate) {
        return Completable.fromAction(() -> repo.newValue(bitRate));
    }
}
