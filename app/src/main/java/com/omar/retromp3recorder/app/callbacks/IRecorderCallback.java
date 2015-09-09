package com.omar.retromp3recorder.app.callbacks;

/**
 * Created by omar on 18.08.15.
 */
public interface IRecorderCallback {
    void onErrorOccured(String error);

    void normalMessage(String message);

    void sendRecorderId(int id);

}
