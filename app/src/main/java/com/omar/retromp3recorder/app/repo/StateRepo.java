package com.omar.retromp3recorder.app.repo;

import com.omar.retromp3recorder.app.main.MainView;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class StateRepo {
    private final BehaviorSubject<MainView.State> stateSubject = BehaviorSubject
            .createDefault(MainView.State.Idle);

    @Inject
    public StateRepo(){}

    public void newValue(MainView.State state) {
        stateSubject.onNext(state);
    }

    public Observable<MainView.State> observe() {
        return stateSubject;
    }
}
