package com.omar.retromp3recorder.app.main;


import com.omar.retromp3recorder.app.di.Interactor;
import com.omar.retromp3recorder.app.repo.BitRateRepo;
import com.omar.retromp3recorder.app.repo.SampleRateRepo;
import com.omar.retromp3recorder.app.usecase.ChangeBitrateUC;
import com.omar.retromp3recorder.app.usecase.ChangeSampleRateUC;
import com.omar.retromp3recorder.app.usecase.ShareUC;
import com.omar.retromp3recorder.app.usecase.StartPlaybackUC;
import com.omar.retromp3recorder.app.usecase.StopPlaybackAndRecordUC;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

import static com.omar.retromp3recorder.app.main.MainView.*;
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


    public MainViewInteractor(
            Scheduler scheduler,
            ChangeBitrateUC changeBitrateUC,
            ChangeSampleRateUC changeSampleRateUC,
            ShareUC shareUC,
            StartPlaybackUC startPlaybackUC,
            StopPlaybackAndRecordUC stopPlaybacAndRecordkUC,
            BitRateRepo bitRateRepo, SampleRateRepo sampleRateRepo) {
        this.scheduler = scheduler;
        this.changeBitrateUC = changeBitrateUC;
        this.changeSampleRateUC = changeSampleRateUC;
        this.shareUC = shareUC;
        this.startPlaybackUC = startPlaybackUC;
        this.stopPlaybacAndRecordkUC = stopPlaybacAndRecordkUC;
        this.bitRateRepo = bitRateRepo;
        this.sampleRateRepo = sampleRateRepo;
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
                                            .map((Function<BitRate, MainViewResult>) BitrateChangedResult::new),
                                    sampleRateRepo
                                            .observe()
                                            .map((Function<SampleRate, MainViewResult>) SampleRateChangeResult::new)
                            ));
                });
    }
}
