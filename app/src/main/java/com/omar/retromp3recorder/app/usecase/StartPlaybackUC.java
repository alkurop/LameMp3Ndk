package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.di.AudioPlayer;
import com.omar.retromp3recorder.app.di.VoiceRecorder;
import com.omar.retromp3recorder.app.main.MainView;
import com.omar.retromp3recorder.app.repo.FileNameRepo;
import com.omar.retromp3recorder.app.repo.StateRepo;

import io.reactivex.Completable;

public class StartPlaybackUC {

    private final FileNameRepo fileNameRepo;
    private final AudioPlayer audioPlayer;
    private final VoiceRecorder voiceRecorder;
    private final StateRepo stateRepo;
    private final StopPlaybackAndRecordUC stopUC;

    public StartPlaybackUC(
            FileNameRepo fileNameRepo,
            AudioPlayer audioPlayer,
            VoiceRecorder voiceRecorder,
            StateRepo stateRepo,
            StopPlaybackAndRecordUC stopUC
    ) {
        this.fileNameRepo = fileNameRepo;
        this.audioPlayer = audioPlayer;
        this.voiceRecorder = voiceRecorder;
        this.stateRepo = stateRepo;
        this.stopUC = stopUC;
    }

    public Completable execute() {
        return stopUC.execute()
                .andThen(
                        fileNameRepo
                                .observe()
                                .take(1)
                                .flatMapCompletable(fileName -> Completable
                                        .fromAction(() -> {
                                            voiceRecorder.stopRecord();
                                            audioPlayer.playerStart(fileName);
                                            stateRepo.newValue(MainView.State.Playing);
                                        })
                                ));
    }
}
