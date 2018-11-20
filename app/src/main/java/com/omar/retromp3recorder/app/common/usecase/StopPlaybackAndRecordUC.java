package com.omar.retromp3recorder.app.common.usecase;

import com.omar.retromp3recorder.app.common.repo.StateRepo;
import com.omar.retromp3recorder.app.main.MainView;
import com.omar.retromp3recorder.app.playback.usecase.StopPlaybackUC;
import com.omar.retromp3recorder.app.recording.usecase.StopRecordUC;

import javax.inject.Inject;

import io.reactivex.Completable;

public class StopPlaybackAndRecordUC implements NoParamsUseCase {

    private final StopRecordUC stopRecordUC;
    private final StopPlaybackUC stopPlaybackUC;
    private final StateRepo stateRepo;

    //region constructor
    @Inject
    public StopPlaybackAndRecordUC(
            StopRecordUC stopRecordUC,
            StopPlaybackUC stopPlaybackUC,
            StateRepo stateRepo
    ) {
        this.stopRecordUC = stopRecordUC;
        this.stopPlaybackUC = stopPlaybackUC;
        this.stateRepo = stateRepo;
    }
    //endregion

    public Completable execute() {
        return stateRepo.observe().take(1)
                .flatMapCompletable(state -> {
                    if(state == MainView.State.Playing){
                        return stopPlaybackUC.execute();
                    }
                    else if (state == MainView.State.Recording){
                        return stopRecordUC.execute();
                    }
                    else {
                        return Completable.complete();
                    }
                });
    }
}
