package com.omar.retromp3recorder.app.repo;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class FileNameRepo {
    private BehaviorSubject<String> stateSubject = BehaviorSubject.create();

    public void setValue(String bitRate) {
        stateSubject.onNext(bitRate);
    }

    public Observable<String> observe() {
        return stateSubject;
    }
}
