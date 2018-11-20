package com.omar.retromp3recorder.app.recording.usecase;

import com.omar.retromp3recorder.app.common.usecase.NoParamsUseCase;
import com.omar.retromp3recorder.app.files.usecase.LookForFilesUC;
import com.omar.retromp3recorder.app.playback.player.AudioPlayer;
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder;

import javax.inject.Inject;

import io.reactivex.Completable;

public class StopRecordUC implements NoParamsUseCase {

    private final AudioPlayer audioPlayer;
    private final VoiceRecorder voiceRecorder;
    private final LookForFilesUC lookForFilesUC;

    //region constructor
    @Inject
    public StopRecordUC(
            AudioPlayer audioPlayer,
            VoiceRecorder voiceRecorder,
            LookForFilesUC lookForFilesUC1) {
        this.audioPlayer = audioPlayer;
        this.voiceRecorder = voiceRecorder;
        this.lookForFilesUC = lookForFilesUC1;
    }
    //endregion

    public Completable execute() {
        return Completable
                .fromAction(() -> {
                    voiceRecorder.stopRecord();
                    audioPlayer.playerStop();
                })
                .andThen(lookForFilesUC.execute());
    }
}
