package com.omar.retromp3recorder.app.recording.repo;

import com.omar.retromp3recorder.app.recording.recorder.RecorderDefaults;
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

@Singleton
public class BitRateRepo {

    private final BehaviorSubject<VoiceRecorder.BitRate> stateSubject = BehaviorSubject.create();

    @Inject
    BitRateRepo(RecorderDefaults defaults){
        stateSubject.onNext(defaults.getBitRate());
    }

    public void newValue(VoiceRecorder.BitRate bitRate) {
        stateSubject.onNext(bitRate);
    }

    public Observable<VoiceRecorder.BitRate> observe() {
        return stateSubject;
    }
}
