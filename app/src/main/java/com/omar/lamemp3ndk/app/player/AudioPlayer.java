package com.omar.lamemp3ndk.app.player;

import android.media.MediaPlayer;
import com.omar.lamemp3ndk.app.callbacks.IPlayerCallback;

import java.io.IOException;

/**
 * Created by omar on 18.08.15.
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private MediaPlayer mediaPlayer;
    private IPlayerCallback callback;
    private String voiceURL;


    public AudioPlayer(String _voiceURL, IPlayerCallback _callback) {
        voiceURL = _voiceURL;
        callback = _callback;
    }

    public void playerStop(){stopMedia();}
    public void playerStart(){setupMediaPlayer(voiceURL);}



    private void setupMediaPlayer(String voiceURL) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        try {
            mediaPlayer.setDataSource(voiceURL);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            callback.OnErrorOccured("Not recorded yet");
        }

    }

    private  void stopMedia() {
        if (mediaPlayer != null ) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            callback.NormalMessage("stoped playing");
            callback.OnAudioEndedAndStoped();
        } else {
            callback.OnErrorOccured("player error onStop");
        }
    }

    private void playMedia() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            callback.SendPlayerId(mediaPlayer.getAudioSessionId());
            callback.NormalMessage("started playing");
        } else {
            callback.OnErrorOccured("player error onPlay");
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopMedia();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        playMedia();

    }


}
