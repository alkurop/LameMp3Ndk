package com.omar.lamemp3ndk.app.controllers;

/**
 * Created by omar on 17.08.15.
 */
abstract public class StateSelector {




    private enum states {Stoped, Recodring, Playing}

    private states currentState = states.Stoped;


    public abstract void StartRecording();

    public abstract void StartPlaying();

    public abstract void StopPlaying();

    public abstract void StopRecodring();

    public abstract void OnCallbackStop();



    public void RecordingClicked() {
        switch (currentState) {
            case Stoped:
                currentState = states.Recodring;
                StartRecording();
                break;
            case Recodring:
                currentState = states.Stoped;
                StopRecodring();
                break;
            case Playing:
                currentState = states.Recodring;
                StopPlaying();
                StartRecording();
                break;

        }
    }

    public void StopAll(){
        switch (currentState) {

            case Recodring:
                currentState = states.Playing;
                StopRecodring();
                break;
            case Playing:
                currentState = states.Stoped;
                StopPlaying();
                break;

        }
    }

    public void PlayCLicked() {
        switch (currentState) {
            case Stoped:
                currentState = states.Playing;
                StartPlaying();
                break;
            case Recodring:
                currentState = states.Playing;
                StopRecodring();
                StartPlaying();
                break;
            case Playing:
                currentState = states.Stoped;
                StopPlaying();
                break;

        }
    }

    public void CallbackStop(){currentState = states.Stoped;
        OnCallbackStop();}
}
