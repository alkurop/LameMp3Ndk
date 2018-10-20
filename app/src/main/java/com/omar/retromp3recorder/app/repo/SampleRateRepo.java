package com.omar.retromp3recorder.app.repo;

import com.omar.retromp3recorder.app.main.MainView;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class SampleRateRepo {
    private BehaviorSubject<MainView.SampleRate> stateSubject = BehaviorSubject.create();

    public void setValue(MainView.SampleRate bitRate) {
        stateSubject.onNext(bitRate);
    }

    public Observable<MainView.SampleRate> observe() {
        return stateSubject;
    }
}
