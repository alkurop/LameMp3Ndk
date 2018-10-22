package com.omar.retromp3recorder.app.share;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;

public class TestSharingModule implements SharingModule {

    @Inject
    public TestSharingModule() {
    }

    @Override
    public Completable share() {
        return Completable.complete();
    }

    @Override
    public Observable<Event> observeEvents() {
        return Observable.never();
    }
}