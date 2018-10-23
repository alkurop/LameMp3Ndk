package com.omar.retromp3recorder.app.player;

import com.omar.retromp3recorder.app.main.MainView;
import com.omar.retromp3recorder.app.repo.PlayerIdRepo;
import com.omar.retromp3recorder.app.repo.StateRepo;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_BETA;
import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

@Singleton
public class StateLoggingAudioPlayer implements AudioPlayer {
    private final AudioPlayer audioPlayer;

    @Inject
    StateLoggingAudioPlayer(
            StateRepo stateRepo,
            @Named(DECORATOR_BETA) AudioPlayer audioPlayer,
            PlayerIdRepo playerIdRepo,
            Scheduler scheduler) {
        this.audioPlayer = audioPlayer;

        Completable stateCompletable = Observable
                .merge(createLinkedList(
                        audioPlayer.observeEvents()
                                .ofType(Error.class)
                                .map(error -> MainView.State.Idle),

                        audioPlayer.observeEvents()
                                .ofType(PlaybackEnded.class)
                                .map(error -> MainView.State.Idle))
                )
                .flatMapCompletable(state -> Completable.fromAction(() ->
                        stateRepo.newValue(state)
                ));

        Completable playerIdCompletable = audioPlayer
                .observeEvents()
                .ofType(SendPlayerId.class)
                .flatMapCompletable(sendPlayerId -> Completable.fromAction(() ->
                        playerIdRepo.newValue(sendPlayerId.playerId)
                ));

        Completable.merge(createLinkedList(stateCompletable, playerIdCompletable))
                .subscribeOn(scheduler)
                .subscribe();
    }

    @Override
    public void playerStop() {
        audioPlayer.playerStop();
    }

    @Override
    public void playerStart(String voiceURL) {
        audioPlayer.playerStart(voiceURL);
    }

    @Override
    public Observable<Event> observeEvents() {
        return audioPlayer.observeEvents();
    }
}
