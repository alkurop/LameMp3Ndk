package com.omar.retromp3recorder.app.main;


import com.omar.retromp3recorder.app.di.Interactor;
import com.omar.retromp3recorder.app.di.VoiceRecorder;
import com.omar.retromp3recorder.app.repo.BitRateRepo;
import com.omar.retromp3recorder.app.repo.SampleRateRepo;
import com.omar.retromp3recorder.app.repo.StateRepo;
import com.omar.retromp3recorder.app.usecase.ChangeBitrateUC;
import com.omar.retromp3recorder.app.usecase.ChangeSampleRateUC;
import com.omar.retromp3recorder.app.usecase.ShareUC;
import com.omar.retromp3recorder.app.usecase.StartPlaybackUC;
import com.omar.retromp3recorder.app.usecase.StopPlaybackAndRecordUC;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

import static com.omar.retromp3recorder.app.main.MainView.BitRateChangeAction;
import static com.omar.retromp3recorder.app.main.MainView.BitrateChangedResult;
import static com.omar.retromp3recorder.app.main.MainView.InitialAction;
import static com.omar.retromp3recorder.app.main.MainView.MainViewAction;
import static com.omar.retromp3recorder.app.main.MainView.MainViewResult;
import static com.omar.retromp3recorder.app.main.MainView.PlayAction;
import static com.omar.retromp3recorder.app.main.MainView.SampleRateChangeAction;
import static com.omar.retromp3recorder.app.main.MainView.SampleRateChangeResult;
import static com.omar.retromp3recorder.app.main.MainView.ShareAction;
import static com.omar.retromp3recorder.app.main.MainView.State;
import static com.omar.retromp3recorder.app.main.MainView.StopAction;
import static com.omar.retromp3recorder.app.utils.ListFromVararg.createList;

public class MainViewInteractor implements Interactor<MainViewAction, MainViewResult> {

    private final Scheduler scheduler;
    private final ChangeSampleRateUC changeSampleRateUC;
    private final ShareUC shareUC;
    private final StartPlaybackUC startPlaybackUC;
    private final StopPlaybackAndRecordUC stopPlaybacAndRecordkUC;
    private final BitRateRepo bitRateRepo;
    private final SampleRateRepo sampleRateRepo;
    private final ChangeBitrateUC changeBitrateUC;
    private final StateRepo stateRepo;


    public MainViewInteractor(
            Scheduler scheduler,
            ChangeBitrateUC changeBitrateUC,
            ChangeSampleRateUC changeSampleRateUC,
            ShareUC shareUC,
            StartPlaybackUC startPlaybackUC,
            StopPlaybackAndRecordUC stopPlaybacAndRecordkUC,
            BitRateRepo bitRateRepo, SampleRateRepo sampleRateRepo, StateRepo stateRepo) {
        this.scheduler = scheduler;
        this.changeBitrateUC = changeBitrateUC;
        this.changeSampleRateUC = changeSampleRateUC;
        this.shareUC = shareUC;
        this.startPlaybackUC = startPlaybackUC;
        this.stopPlaybacAndRecordkUC = stopPlaybacAndRecordkUC;
        this.bitRateRepo = bitRateRepo;
        this.sampleRateRepo = sampleRateRepo;
        this.stateRepo = stateRepo;
    }

    @Override
    public ObservableTransformer<MainViewAction, MainViewResult> process() {
        return upstream -> upstream.observeOn(scheduler)
                .compose((ObservableTransformer<MainViewAction, MainViewResult>) actions -> {
                    return Observable.merge(
                            createList(
                                    actions
                                            .ofType(PlayAction.class)
                                            .flatMapCompletable(playAction -> startPlaybackUC.execute())
                                            .toObservable(),
                                    actions
                                            .ofType(InitialAction.class)
                                            .flatMapCompletable(initialAction -> Completable
                                                    .fromAction(() -> stateRepo.newValue(State.Idle))
                                            )
                                            .toObservable(),
                                    actions
                                            .ofType(ShareAction.class)
                                            .flatMapCompletable(shareAction -> shareUC.execute())
                                            .toObservable(),
                                    actions
                                            .ofType(StopAction.class)
                                            .flatMapCompletable(stopAction -> stopPlaybacAndRecordkUC.execute())
                                            .toObservable(),
                                    actions
                                            .ofType(SampleRateChangeAction.class)
                                            .flatMapCompletable(sampleRateChangeAction ->
                                                    changeSampleRateUC.execute(sampleRateChangeAction.sampleRate))
                                            .toObservable(),
                                    actions
                                            .ofType(BitRateChangeAction.class)
                                            .flatMapCompletable(bitRateChangeAction ->
                                                    changeBitrateUC.execute(bitRateChangeAction.bitRate))
                                            .toObservable(),
                                    bitRateRepo
                                            .observe()
                                            .map((Function<VoiceRecorder.BitRate, MainViewResult>) BitrateChangedResult::new),
                                    sampleRateRepo
                                            .observe()
                                            .map((Function<VoiceRecorder.SampleRate, MainViewResult>) SampleRateChangeResult::new)

                            ));
                });
    }
}
