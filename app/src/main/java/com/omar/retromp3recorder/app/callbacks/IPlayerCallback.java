package com.omar.retromp3recorder.app.callbacks;

/**
 * Created by omar on 18.08.15.
 */
public interface IPlayerCallback {
    void OnErrorOccured (String error);

    void OnAudioEndedAndStoped();

    void NormalMessage(String message);

    void SendPlayerId(int id);
}