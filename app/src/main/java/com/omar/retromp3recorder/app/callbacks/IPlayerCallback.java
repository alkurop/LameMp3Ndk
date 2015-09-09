package com.omar.retromp3recorder.app.callbacks;

/**
 * Created by omar on 18.08.15.
 */
public interface IPlayerCallback {
    void onErrorOccured(String error);

    void onAudioEndedAndStoped();

    void normalMessage(String message);

    void sendPlayerId(int id);
}
