package com.omar.retromp3recorder.app.repo;

import com.omar.retromp3recorder.app.recorder.RecorderDefaults;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class SampleRateRepo {
    private final BehaviorSubject<VoiceRecorder.SampleRate> stateSubject = BehaviorSubject.create();

    @Inject
    SampleRateRepo(RecorderDefaults defaults){
        stateSubject.onNext(defaults.sampleRate);
    }

    public void newValue( VoiceRecorder.SampleRate bitRate) {
        stateSubject.onNext(bitRate);
    }

    public Observable<VoiceRecorder.SampleRate> observe() {
        return stateSubject;
    }
}
