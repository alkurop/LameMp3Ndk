package com.omar.retromp3recorder.app.player;

import javax.inject.Inject;

import io.reactivex.Observable;

public class TestAudioPlayer implements AudioPlayer {

    @Inject
    public TestAudioPlayer() {
    }

    @Override
    public void playerStop() {

    }

    @Override
    public void playerStart(String voiceURL) {

    }

    @Override
    public Observable<Event> observeEvents() {
        return null;
    }
}
