package com.omar.retromp3recorder.app.controllers;

/**
 * Created by omar on 17.08.15.
 */
abstract public class StateSelector {

    private enum STATES {STOPPED, RECORDING, PLAYING}
    private STATES currentState = STATES.STOPPED;

    public void RecordingClicked() {
        switch (currentState) {
            case STOPPED:
                currentState = STATES.RECORDING;
                StartRecording();
                break;
            case RECORDING:
                currentState = STATES.STOPPED;
                StopRecodring();
                break;
            case PLAYING:
                currentState = STATES.RECORDING;
                StopPlaying();
                StartRecording();
                break;
        }
    }

    public void StopAll(){
        switch (currentState) {
            case RECORDING:
                currentState = STATES.PLAYING;
                StopRecodring();
                break;
            case PLAYING:
                currentState = STATES.STOPPED;
                StopPlaying();
                break;
        }
    }

    public void PlayCLicked() {
        switch (currentState) {
            case STOPPED:
                currentState = STATES.PLAYING;
                StartPlaying();
                break;
            case RECORDING:
                currentState = STATES.PLAYING;
                StopRecodring();
                StartPlaying();
                break;
            case PLAYING:
                currentState = STATES.STOPPED;
                StopPlaying();
                break;
        }
    }

    public void CallbackStop(){currentState = STATES.STOPPED;
        OnCallbackStop();}


    public abstract void StartRecording();

    public abstract void StartPlaying();

    public abstract void StopPlaying();

    public abstract void StopRecodring();

    public abstract void OnCallbackStop();
}
