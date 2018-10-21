package com.omar.retromp3recorder.app.player;

import com.omar.retromp3recorder.app.logger.LogRepo;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

public class LoggingAudioPlayer implements AudioPlayer {
    private final AudioPlayer audioPlayer;

    public LoggingAudioPlayer(AudioPlayer audioPlayer, Scheduler scheduler, LogRepo logRepo) {
        this.audioPlayer = audioPlayer;
        Observable<AudioPlayer.Event> share = audioPlayer.observeEvents().share();
        Observable<LogRepo.Event> message = share.ofType(AudioPlayer.Message.class)
                .map(answer -> new LogRepo.Message(answer.message));
        Observable<LogRepo.Event> error = share.ofType(AudioPlayer.Error.class)
                .map(answer -> new LogRepo.Message(answer.error));

        Observable
                .merge(createLinkedList(
                        message, error
                ))
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
