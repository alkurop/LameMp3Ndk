package com.omar.lamemp3ndk.app.callbacks;

/**
 * Created by omar on 18.08.15.
 */
public interface IAudioControllerCallback {
    void onPlayerStoped();
    void onRecorderStoped();
    void SetPlayerId(int id);
    void onPlayerError(String s);
    void onRecorderError(String s);
}
