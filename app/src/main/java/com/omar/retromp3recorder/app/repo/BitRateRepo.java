package com.omar.retromp3recorder.app.repo;

import com.omar.retromp3recorder.app.di.VoiceRecorder;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class BitRateRepo {
    private BehaviorSubject<VoiceRecorder.BitRate> stateSubject = BehaviorSubject.create();

    public void newValue( VoiceRecorder.BitRate bitRate) {
        stateSubject.onNext(bitRate);
    }

    public Observable<VoiceRecorder.BitRate> observe() {
        return stateSubject;
    }
}
