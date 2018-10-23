package com.omar.retromp3recorder.app.repo;


import com.omar.retromp3recorder.app.mvi.OneShot;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

@Singleton
public class PlayerIdRepo {
    private final Subject<OneShot<Integer>> events = ReplaySubject.create();

    @Inject
    public PlayerIdRepo(){}

    public Observable<OneShot<Integer>> observe() {
        return events;
    }

    public void newValue(Integer event) {
        events.onNext(new OneShot<>(event));
    }
}
