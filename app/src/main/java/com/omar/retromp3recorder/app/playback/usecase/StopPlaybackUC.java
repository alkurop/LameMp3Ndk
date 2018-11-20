package com.omar.retromp3recorder.app.playback.usecase;

import com.omar.retromp3recorder.app.files.usecase.LookForFilesUC;
import com.omar.retromp3recorder.app.playback.player.AudioPlayer;

import javax.inject.Inject;

import io.reactivex.Completable;

public class StopPlaybackUC  {

    private final AudioPlayer audioPlayer;
    private final LookForFilesUC lookForFilesUC;

    //region constructor
    @Inject
    public StopPlaybackUC(
            AudioPlayer audioPlayer,
            LookForFilesUC lookForFilesUC1) {
        this.audioPlayer = audioPlayer;
        this.lookForFilesUC = lookForFilesUC1;
    }
    //endregion

    public Completable execute() {
        return Completable
                .fromAction(audioPlayer::playerStop)
                .andThen(lookForFilesUC.execute());
    }
}
