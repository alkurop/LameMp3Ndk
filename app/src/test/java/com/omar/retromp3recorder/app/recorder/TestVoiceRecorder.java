package com.omar.retromp3recorder.app.recorder;

import javax.inject.Inject;

import io.reactivex.Observable;

public class TestVoiceRecorder implements VoiceRecorder {
    @Inject
    public TestVoiceRecorder() {
    }

    @Override
    public Observable<Event> observeEvents() {
        return null;
    }

    @Override
    public void record(RecorderProps props) {

    }

    @Override
    public void stopRecord() {

    }
}
