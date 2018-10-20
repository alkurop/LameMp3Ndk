package com.omar.retromp3recorder.app.player;

import android.media.MediaPlayer;

import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.di.AudioPlayer;
import com.omar.retromp3recorder.app.di.StringProvider;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by omar on 18.08.15.
 */
public class AudioPlayerRx implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        AudioPlayer {

    private final StringProvider stringProvider;
    private final PublishSubject<Event> events = PublishSubject.create();
    private MediaPlayer mediaPlayer;

    public AudioPlayerRx(StringProvider context) {
        this.stringProvider = context;
    }

    @Override
    public void playerStop() {
        stopMedia();
    }

    @Override
    public void playerStart(String voiceURL) {
        setupMediaPlayer(voiceURL);
    }

    @Override
    public Observable<Event> observeEvents(){
        return events;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopMedia();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        playMedia();
    }

    private void setupMediaPlayer(String voiceURL) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        try {
            mediaPlayer.setDataSource(voiceURL);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            events.onNext(new Error(stringProvider.getString(R.string.not_recorder_yet)));
        }
    }

    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            events.onNext(new Message(stringProvider.getString(R.string.stopped_playing)));
            events.onNext(new PlaybackEnded());
        } else {
            events.onNext(new Error(stringProvider.getString(R.string.player_error_on_stop)));
        }
    }

    private void playMedia() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            events.onNext(new SendPlayerId(mediaPlayer.getAudioSessionId()));
            events.onNext(new Message(stringProvider.getString(R.string.started_playing)));
        } else {
            events.onNext(new Error(stringProvider.getString(R.string.player_erro_on_stop)));
        }
    }
}
