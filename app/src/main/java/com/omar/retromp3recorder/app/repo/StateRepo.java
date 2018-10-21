package com.omar.retromp3recorder.app.repo;

import com.omar.retromp3recorder.app.main.MainView;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class StateRepo {
    private BehaviorSubject<MainView.State> stateSubject = BehaviorSubject.create();

    public void newValue(MainView.State state) {
        stateSubject.onNext(state);
    }

    public Observable<MainView.State> observe() {
        return stateSubject;
    }
}
