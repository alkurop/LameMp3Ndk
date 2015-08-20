package com.omar.lamemp3ndk.app.player;

import android.content.Context;
import android.media.MediaPlayer;
import com.omar.lamemp3ndk.app.R;
import com.omar.lamemp3ndk.app.callbacks.IPlayerCallback;
import com.omar.lamemp3ndk.app.utils.ContextHelper;

import java.io.IOException;

/**
 * Created by omar on 18.08.15.
 */
public class AudioPlayer implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private Context context;
    private IPlayerCallback callback;

    private MediaPlayer mediaPlayer;

    private String voiceURL;


    public AudioPlayer(String _voiceURL, IPlayerCallback _callback) {
        context = ContextHelper.GetContext();
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
            callback.OnErrorOccured(context.getString(R.string.not_recorder_yet));
        }
    }

    private void stopMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            callback.NormalMessage(context.getString(R.string.stopped_playing));
            callback.OnAudioEndedAndStoped();
        } else {
            callback.OnErrorOccured(context.getString(R.string.player_error_on_stop));
        }
    }

    private void playMedia() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            callback.SendPlayerId(mediaPlayer.getAudioSessionId());
            callback.NormalMessage(context.getString(R.string.started_playing));
        } else {
            callback.OnErrorOccured(context.getString(R.string.player_erro_on_stop));
        }
    }


}
