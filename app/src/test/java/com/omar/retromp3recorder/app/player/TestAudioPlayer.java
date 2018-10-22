package com.omar.retromp3recorder.app.player;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

import static com.omar.retromp3recorder.app.di.TestFunctionalityModule.PLAYER_BUS;

public class TestAudioPlayer implements AudioPlayer {

    private final Subject<Event> audioBus;

    @Inject
    public TestAudioPlayer(@Named(PLAYER_BUS) Subject<Event> audioBus) {
        this.audioBus = audioBus;
    }

    @Override
    public void playerStop() {

    }

    @Override
    public void playerStart(String voiceURL) {

    }

    @Override
    public Observable<Event> observeEvents() {
        return audioBus;
    }
}
