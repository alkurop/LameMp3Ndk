package com.omar.retromp3recorder.app.player;

import android.media.MediaPlayer;

import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.stringer.Stringer;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by omar on 18.08.15.
 */
public final class AudioPlayerRx implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        AudioPlayer {

    private final Stringer stringer;
    private final PublishSubject<Event> events = PublishSubject.create();
    private MediaPlayer mediaPlayer;

    @Inject
    public AudioPlayerRx(Stringer stringer) {
        this.stringer = stringer;
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
        if (!new File(voiceURL).exists()) {
            events.onNext(new Error(stringer.getString(R.string.player_cannot_find_file)));
            return;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        try {
            mediaPlayer.setDataSource(voiceURL);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            events.onNext(new Error(stringer.getString(R.string.not_recorder_yet)));
        }
    }

    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            events.onNext(new Message(stringer.getString(R.string.stopped_playing)));
            events.onNext(new PlaybackEnded());
        } else {
            events.onNext(new Error(stringer.getString(R.string.player_error_on_stop)));
        }
    }

    private void playMedia() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            events.onNext(new SendPlayerId(mediaPlayer.getAudioSessionId()));
            events.onNext(new Message(stringer.getString(R.string.started_playing)));
        } else {
            events.onNext(new Error(stringer.getString(R.string.player_erro_on_stop)));
        }
    }
}
