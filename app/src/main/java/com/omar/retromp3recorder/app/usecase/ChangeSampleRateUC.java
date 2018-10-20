package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.main.MainView;

import io.reactivex.Completable;

public class ChangeSampleRateUC {
    public Completable execute(MainView.SampleRate sampleRate) {
        return Completable.complete();
    }
}
