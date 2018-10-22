package com.omar.retromp3recorder.app.recorder;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

import static com.omar.retromp3recorder.app.di.TestFunctionalityModule.RECORDER_BUS;

public class TestVoiceRecorder implements VoiceRecorder {
    private final Subject<Event> bus;

    @Inject
    public TestVoiceRecorder(@Named(RECORDER_BUS) Subject<Event> audioBus) {
        this.bus = audioBus;
    }

    @Override
    public Observable<Event> observeEvents() {
        return bus;
    }

    @Override
    public void record(RecorderProps props) {

    }

    @Override
    public void stopRecord() {

    }
}