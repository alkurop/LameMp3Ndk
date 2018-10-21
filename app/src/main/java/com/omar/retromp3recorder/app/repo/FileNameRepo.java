package com.omar.retromp3recorder.app.repo;

import com.omar.retromp3recorder.app.di.RecorderDefaults;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class FileNameRepo {
    private final BehaviorSubject<String> stateSubject = BehaviorSubject.create();

    @Inject
    public FileNameRepo(RecorderDefaults defaults){
        stateSubject.onNext(defaults.filePath);
    }

    public void newValue(String bitRate) {
        stateSubject.onNext(bitRate);
    }

    public Observable<String> observe() {
        return stateSubject;
    }

}
