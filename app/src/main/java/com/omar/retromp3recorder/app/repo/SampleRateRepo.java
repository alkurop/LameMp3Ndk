package com.omar.retromp3recorder.app.repo;

import com.omar.retromp3recorder.app.di.VoiceRecorder;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class SampleRateRepo {
    private BehaviorSubject<VoiceRecorder.SampleRate> stateSubject = BehaviorSubject.create();

    public void newValue( VoiceRecorder.SampleRate bitRate) {
        stateSubject.onNext(bitRate);
    }

    public Observable<VoiceRecorder.SampleRate> observe() {
        return stateSubject;
    }
}
