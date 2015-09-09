package com.omar.retromp3recorder.app.controllers;

/**
 * Created by omar on 17.08.15.
 */
abstract public class StateSelector {

    private enum STATES {STOPPED, RECORDING, PLAYING}
    private STATES currentState = STATES.STOPPED;

    public void recordingClicked() {
        switch (currentState) {
            case STOPPED:
                currentState = STATES.RECORDING;
                startRecording();
                break;
            case RECORDING:
                currentState = STATES.STOPPED;
                stopRecodring();
                break;
            case PLAYING:
                currentState = STATES.RECORDING;
                stopPlaying();
                startRecording();
                break;
        }
    }

    public void stopAll(){
        switch (currentState) {
            case RECORDING:
                currentState = STATES.PLAYING;
                stopRecodring();
                break;
            case PLAYING:
                currentState = STATES.STOPPED;
                stopPlaying();
                break;
        }
    }

    public void playCLicked() {
        switch (currentState) {
            case STOPPED:
                currentState = STATES.PLAYING;
                startPlaying();
                break;
            case RECORDING:
                currentState = STATES.PLAYING;
                stopRecodring();
                startPlaying();
                break;
            case PLAYING:
                currentState = STATES.STOPPED;
                stopPlaying();
                break;
        }
    }

    public void callbackStop(){currentState = STATES.STOPPED;
        onCallbackStop();}


    public abstract void startRecording();

    public abstract void startPlaying();

    public abstract void stopPlaying();

    public abstract void stopRecodring();

    public abstract void onCallbackStop();
}
