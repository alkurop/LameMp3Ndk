package com.omar.lamemp3ndk.app.controllers;

import android.content.Context;
import com.omar.lamemp3ndk.app.callbacks.IAudioControllerCallback;
import com.omar.lamemp3ndk.app.callbacks.IPlayerCallback;
import com.omar.lamemp3ndk.app.callbacks.IRecorderCallback;
import com.omar.lamemp3ndk.app.player.AudioPlayer;
import com.omar.lamemp3ndk.app.recorder.VoiceRecorder;
import com.omar.lamemp3ndk.app.utils.ContextHelper;

import java.io.File;


/**
 * Created by omar on 17.08.15.
 */
public class AudioStatesController implements IAudioStatesEvents {
    private ILsdDisplay lsdDisplay;
    private String filePath;

    private VoiceRecorder voiceRecorder;
    private AudioPlayer audioPlayer;

    private IAudioControllerCallback upperLevelCallback;
    private IPlayerCallback playerCallback;
    private IRecorderCallback recorderCallback;
    private int currentBPM;
    private int currentHz;

    @Override
    public void StartRecord() {
        voiceRecorder = new VoiceRecorder(filePath, recorderCallback);

        voiceRecorder.recordStart(currentHz, currentBPM);
    }

    @Override
    public void StartPlay() {
        if (checkIfFileExists(filePath)) {
            audioPlayer = new AudioPlayer(filePath, playerCallback);
            audioPlayer.playerStart();
        } else {
            upperLevelCallback.onPlayerError("Player cannot find file");
        }
    }

    @Override
    public void StopRecord() {
        voiceRecorder.recordStop();
    }

    @Override
    public void StopPlay() {
        audioPlayer.playerStop();
    }


    @Override
    public void Init(ILsdDisplay _lsdDisplay, IAudioControllerCallback _callback) {
        lsdDisplay = _lsdDisplay;
        upperLevelCallback = _callback;
        filePath = genrateFilePath(ContextHelper.GetContext());
        initPlayerCallback();
        initRecorderCallback();
    }

    @Override
    public void SetReсorderBPM(int _bpm) {
        currentBPM = _bpm;
    }

    @Override
    public void SetReсorderHz(int _hz) {
        currentHz = _hz;
    }

    @Override
    public String GetFilePath() {
        return filePath;
    }

    private String genrateFilePath(Context _context) {

        String subDir = "voice_record";
        String fileName = subDir + ".mp3";
        String filepath = _context.getCacheDir() + "/" + fileName;
        return filepath;
    }

    private boolean checkIfFileExists(String _filepath) {
        File file = new File(_filepath);
        if (file.exists()) return true;
        else return false;
    }

    private void initRecorderCallback() {
        recorderCallback = new IRecorderCallback() {
            @Override
            public void OnErrorOccured(String error) {
                upperLevelCallback.onRecorderError(error);
            }

            @Override
            public void NormalMessage(String message) {
                lsdDisplay.SetText(message);
            }

            @Override
            public void SendRecorderId(int id) {
                upperLevelCallback.SetPlayerId(id);
            }
        };
    }

    private void initPlayerCallback() {
        playerCallback = new IPlayerCallback() {
            @Override
            public void OnErrorOccured(String error) {
                upperLevelCallback.onPlayerError(error);
            }

            @Override
            public void OnAudioEndedAndStoped() {
                upperLevelCallback.onPlayerStoped();
            }

            @Override
            public void NormalMessage(String message) {
                lsdDisplay.SetText(message);
            }

            @Override
            public void SendPlayerId(int id) {
                upperLevelCallback.SetPlayerId(id);
            }
        };
    }

}
