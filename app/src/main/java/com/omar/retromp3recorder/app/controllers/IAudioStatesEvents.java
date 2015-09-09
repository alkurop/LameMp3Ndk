package com.omar.retromp3recorder.app.controllers;

import com.omar.retromp3recorder.app.callbacks.IAudioControllerCallback;

/**
 * Created by omar on 17.08.15.
 */
public interface IAudioStatesEvents {

    void startRecord();

    void startPlay();

    void stopRecord();

    void stopPlay();

    void init(ILsdDisplay lsdDisplay, IAudioControllerCallback callback);

    void setReсorderBPM(int _bpm);


    void setReсorderHz(int _hz);

    String getFilePath();
}
