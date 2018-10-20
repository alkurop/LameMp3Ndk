package com.omar.retromp3recorder.app.usecase;

import io.reactivex.Completable;

public class StopPlaybackAndRecordUC {
    public Completable execute() {
        return Completable.complete();
    }
}
