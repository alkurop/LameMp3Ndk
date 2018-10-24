package com.omar.retromp3recorder.app.player;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.subjects.Subject;

import static com.omar.retromp3recorder.app.di.MockModule.PLAYER_SUBJECT;

public class TestAudioPlayer implements AudioPlayer {

    private final Subject<Event> audioBus;

    @Inject
    TestAudioPlayer(@Named(PLAYER_SUBJECT) Subject<Event> audioBus) {
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
