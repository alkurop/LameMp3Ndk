package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.main.MainView;
import com.omar.retromp3recorder.app.repo.StateRepo;

import javax.inject.Inject;

import io.reactivex.Completable;

public class StopPlaybackAndRecordUCImpl implements StopPlaybackAndRecordUC {

    private final AudioPlayer audioPlayer;
    private final VoiceRecorder voiceRecorder;

    //region constructor
    @Inject
    public StopPlaybackAndRecordUCImpl(
            AudioPlayer audioPlayer,
            VoiceRecorder voiceRecorder
    ) {
        this.audioPlayer = audioPlayer;
        this.voiceRecorder = voiceRecorder;
    }
    //endregion

    public Completable execute() {
        return Completable
                .fromAction(() -> {
                    voiceRecorder.stopRecord();
                    audioPlayer.playerStop();
                });
    }
}
