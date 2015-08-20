package com.omar.lamemp3ndk.app.controllers;

import com.omar.lamemp3ndk.app.callbacks.IAudioControllerCallback;

/**
 * Created by omar on 17.08.15.
 */
public interface IAudioStatesEvents {

    void StartRecord();

    void StartPlay();

    void StopRecord();

    void StopPlay();

    void Init(ILsdDisplay lsdDisplay, IAudioControllerCallback callback);

    void SetReсorderBPM(int _bpm);


    void SetReсorderHz(int _hz);

    String GetFilePath();
}
