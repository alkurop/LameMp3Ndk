package com.omar.lamemp3ndk.app.callbacks;

/**
 * Created by omar on 18.08.15.
 */
public interface IRecorderCallback {
    void OnErrorOccured(String error);

    void NormalMessage(String message);

    void SendRecorderId(int id);

}
