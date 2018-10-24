package com.omar.retromp3recorder.app.share;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

import static com.omar.retromp3recorder.app.di.MockModule.SHARING_SUBJECT;

public class TestSharingModule implements SharingModule {

    private final Subject<Event> events;

    @Inject
    TestSharingModule(@Named(SHARING_SUBJECT) Subject<Event> events) {
        this.events = events;
    }

    @Override
    public Completable share() {
        return Completable.complete();
    }

    @Override
    public Observable<Event> observeEvents() {
        return events;
    }
}
