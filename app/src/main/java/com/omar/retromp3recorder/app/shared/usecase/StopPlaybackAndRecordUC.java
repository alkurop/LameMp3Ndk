package com.omar.retromp3recorder.app.shared.usecase;

import com.omar.retromp3recorder.app.playback.player.AudioPlayer;
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder;

import javax.inject.Inject;

import io.reactivex.Completable;

public class StopPlaybackAndRecordUC implements NoParamsUseCase {

    private final AudioPlayer audioPlayer;
    private final VoiceRecorder voiceRecorder;

    //region constructor
    @Inject
    public StopPlaybackAndRecordUC(
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
