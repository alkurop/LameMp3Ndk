package com.omar.retromp3recorder.app.files.repo;

import com.omar.retromp3recorder.app.recording.recorder.RecorderDefaults;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class CurrentFileRepo {
    private final BehaviorSubject<String> stateSubject = BehaviorSubject.create();

    @Inject
    CurrentFileRepo(RecorderDefaults defaults) {
        stateSubject.onNext(defaults.filePath);
    }

    public void newValue(String bitRate) {
        stateSubject.onNext(bitRate);
    }

    public Observable<String> observe() {
        return stateSubject;
    }

    public boolean hasValue() {
        return stateSubject.hasValue();
    }
}
