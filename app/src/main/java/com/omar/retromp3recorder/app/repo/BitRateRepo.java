package com.omar.retromp3recorder.app.repo;

import com.omar.retromp3recorder.app.main.MainView;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class BitRateRepo {
    private BehaviorSubject<MainView.BitRate> stateSubject = BehaviorSubject.create();

    public void setValue(MainView.BitRate bitRate) {
        stateSubject.onNext(bitRate);
    }

    public Observable<MainView.BitRate> observe() {
        return stateSubject;
    }
}
