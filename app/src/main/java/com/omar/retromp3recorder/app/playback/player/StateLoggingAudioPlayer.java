package com.omar.retromp3recorder.app.playback.player;

import com.omar.retromp3recorder.app.main.MainView;
import com.omar.retromp3recorder.app.playback.repo.PlayerIdRepo;
import com.omar.retromp3recorder.app.common.repo.StateRepo;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_B;
import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

@Singleton
public class StateLoggingAudioPlayer implements AudioPlayer {
    private final AudioPlayer audioPlayer;
    private final StateRepo stateRepo;

    @Inject
    StateLoggingAudioPlayer(
            StateRepo stateRepo,
            @Named(DECORATOR_B) AudioPlayer audioPlayer,
            PlayerIdRepo playerIdRepo,
            Scheduler scheduler) {
        this.audioPlayer = audioPlayer;
        this.stateRepo = stateRepo;

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
        if (audioPlayer.isPlaying()) {
            stateRepo.newValue(MainView.State.Idle);
            audioPlayer.playerStop();
        }
    }

    @Override
    public void playerStart(String voiceURL) {
        stateRepo.newValue(MainView.State.Playing);
        audioPlayer.playerStart(voiceURL);
    }

    @Override
    public Observable<Event> observeEvents() {
        return audioPlayer.observeEvents();
    }

    @Override
    public boolean isPlaying() {
        return audioPlayer.isPlaying();
    }
}
