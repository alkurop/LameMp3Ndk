package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.main.MainView;
import com.omar.retromp3recorder.app.repo.StateRepo;

import javax.inject.Inject;

import io.reactivex.Completable;

public class StopPlaybackAndRecordUC {
    private final AudioPlayer audioPlayer;
    private final VoiceRecorder voiceRecorder;
    private final StateRepo stateRepo;

    @Inject
    public StopPlaybackAndRecordUC(
            AudioPlayer audioPlayer,
            VoiceRecorder voiceRecorder,
            StateRepo stateRepo
    ) {
        this.audioPlayer = audioPlayer;
        this.voiceRecorder = voiceRecorder;
        this.stateRepo = stateRepo;
    }

    public Completable execute() {
        return Completable
                .fromAction(() -> {
                    stateRepo.newValue(MainView.State.Idle);
                    voiceRecorder.stopRecord();
                    audioPlayer.playerStop();
                });
    }
}
