package com.omar.retromp3recorder.app.playback.player;

import com.omar.retromp3recorder.app.common.repo.LogRepo;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_A;
import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

public final class LoggingAudioPlayer implements AudioPlayer {
    private final AudioPlayer audioPlayer;

    @Inject
    LoggingAudioPlayer(
            @Named(DECORATOR_A) AudioPlayer audioPlayer,
            Scheduler scheduler,
            LogRepo logRepo
    ) {
        this.audioPlayer = audioPlayer;
        Observable<AudioPlayer.Event> share = audioPlayer.observeEvents().share();
        Observable<LogRepo.Event> message = share.ofType(AudioPlayer.Message.class)
                .map(answer -> new LogRepo.Event.Message(answer.message));
        Observable<LogRepo.Event> error = share.ofType(AudioPlayer.Error.class)
                .map(answer -> new LogRepo.Event.Error(answer.error));

        Observable
                .merge(createLinkedList(
                        message, error
                ))
                .flatMapCompletable(event -> Completable.fromAction(() ->
                        logRepo.newValue(event)
                ))
                .subscribeOn(scheduler)
                .subscribe();
    }

    @Override
    public void playerStop() {
        if (audioPlayer.isPlaying()) {
            audioPlayer.playerStop();
        }
    }

    @Override
    public void playerStart(String voiceURL) {
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
