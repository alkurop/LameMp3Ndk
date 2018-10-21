package com.omar.retromp3recorder.app.player;

import android.content.Context;
import android.media.MediaPlayer;
import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.callbacks.IPlayerCallback;
import com.omar.retromp3recorder.app.utils.ContextHelper;

import java.io.IOException;

/**
 * Created by omar on 18.08.15.
 */
public class OldAudioPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private Context context;
    private IPlayerCallback callback;

    private MediaPlayer mediaPlayer;

    private String voiceURL;


    public OldAudioPlayer(String _voiceURL, IPlayerCallback _callback) {
        context = ContextHelper.getContext();
        voiceURL = _voiceURL;
        callback = _callback;
    }

    public void playerStop() {
        stopMedia();
    }

    public void playerStart() {
        setupMediaPlayer(voiceURL);
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        stopMedia();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {playMedia();}

    private void setupMediaPlayer(String voiceURL) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnPreparedListener(this);
        try {
            mediaPlayer.setDataSource(voiceURL);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            callback.onErrorOccured(context.getString(R.string.not_recorder_yet));
        }
    }

    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            callback.normalMessage(context.getString(R.string.stopped_playing));
            callback.onAudioEndedAndStoped();
        } else {
            callback.onErrorOccured(context.getString(R.string.player_error_on_stop));
        }
    }

    private void playMedia() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            callback.sendPlayerId(mediaPlayer.getAudioSessionId());
            callback.normalMessage(context.getString(R.string.started_playing));
        } else {
            callback.onErrorOccured(context.getString(R.string.player_erro_on_stop));
        }
    }


}
