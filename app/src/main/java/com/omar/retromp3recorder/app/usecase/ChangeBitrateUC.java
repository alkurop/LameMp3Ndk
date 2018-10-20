package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.main.MainView;

import io.reactivex.Completable;

public class ChangeBitrateUC {

    public Completable execute(MainView.BitRate bitRate) {
        return Completable.complete();
    }
}
