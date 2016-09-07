package com.omar.retromp3recorder.app.controllers;

import android.content.Context;
import com.omar.retromp3recorder.app.Constants;
import com.omar.retromp3recorder.app.R;
import com.omar.retromp3recorder.app.callbacks.IAudioControllerCallback;
import com.omar.retromp3recorder.app.callbacks.IPlayerCallback;
import com.omar.retromp3recorder.app.callbacks.IRecorderCallback;
import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.utils.ContextHelper;

import java.io.File;


/**
 * Created by omar on 17.08.15.
 */
public class AudioStatesController implements IAudioStatesEvents {

    private IAudioControllerCallback upperLevelCallback;
    private IPlayerCallback playerCallback;
    private IRecorderCallback recorderCallback;
    private ILsdDisplay lsdDisplay;

    private VoiceRecorder voiceRecorder;
    private AudioPlayer audioPlayer;

    private String filePath;

    private int currentBPM;
    private int currentHz;

    @Override
    public void startRecord() {
        getVoiceRecorder();
        voiceRecorder.recordStart(currentHz, currentBPM);
    }

    @Override
    public void startPlay() {
        if (checkIfFileExists(filePath)) {
            audioPlayer = new AudioPlayer(filePath, playerCallback);
            audioPlayer.playerStart();
        } else {
            upperLevelCallback.onPlayerError(ContextHelper.getContext().getString(R.string.player_cannot_find_file));
        }
    }

    @Override
    public void stopRecord() {
        voiceRecorder.recordStop();
    }

    @Override
    public void stopPlay() {
        audioPlayer.playerStop();
    }


    @Override
    public void init(ILsdDisplay _lsdDisplay, IAudioControllerCallback _callback) {
        lsdDisplay = _lsdDisplay;
        upperLevelCallback = _callback;
        filePath = genrateFilePath(ContextHelper.getContext());
        initPlayerCallback();
        initRecorderCallback();
    }

    @Override
    public void setReсorderBPM(int _bpm) {
        currentBPM = _bpm;
    }

    @Override
    public void setReсorderHz(int _hz) {
        currentHz = _hz;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    private String genrateFilePath(Context _context) {

        String name = Constants.VOICE_RECORD;
        String fileName = name + Constants.MP3_EXTENTION;
        return _context.getExternalCacheDir() + "/" + fileName;
    }

    private boolean checkIfFileExists(String _filepath) {
        File file = new File(_filepath);
        return file.exists();
    }

    private void initRecorderCallback() {
        recorderCallback = new IRecorderCallback() {
            @Override
            public void onErrorOccured(String error) {
                upperLevelCallback.onRecorderError(error);
            }

            @Override
            public void normalMessage(String message) {
                lsdDisplay.setText(message);
            }

            @Override
            public void sendRecorderId(int id) {
               // upperLevelCallback.SetPlayerId(id);
            }
        };
    }

    private void initPlayerCallback() {
        playerCallback = new IPlayerCallback() {
            @Override
            public void onErrorOccured(String error) {
                upperLevelCallback.onPlayerError(error);
            }

            @Override
            public void onAudioEndedAndStoped() {
                upperLevelCallback.onPlayerStoped();
            }

            @Override
            public void normalMessage(String message) {
                lsdDisplay.setText(message);
            }

            @Override
            public void sendPlayerId(int id) {
                upperLevelCallback.SetPlayerId(id);
            }
        };
    }


    public VoiceRecorder getVoiceRecorder(){
        return voiceRecorder = new VoiceRecorder(filePath, recorderCallback);
    }


    /*FOR UNIT TESTING ONLY*/



}
