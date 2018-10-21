package com.omar.retromp3recorder.app.repo;

import com.omar.retromp3recorder.app.recorder.RecorderDefaults;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class BitRateRepo {

    private final BehaviorSubject<VoiceRecorder.BitRate> stateSubject = BehaviorSubject.create();

    @Inject
    public BitRateRepo(RecorderDefaults defaults){
        stateSubject.onNext(defaults.bitRate);
    }

    public void newValue(VoiceRecorder.BitRate bitRate) {
        stateSubject.onNext(bitRate);
    }

    public Observable<VoiceRecorder.BitRate> observe() {
        return stateSubject;
    }
}
