package com.omar.retromp3recorder.app.main;


import com.omar.retromp3recorder.app.di.Interactor;
import com.omar.retromp3recorder.app.di.VoiceRecorder;
import com.omar.retromp3recorder.app.logger.LogRepo;
import com.omar.retromp3recorder.app.repo.BitRateRepo;
import com.omar.retromp3recorder.app.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.repo.SampleRateRepo;
import com.omar.retromp3recorder.app.repo.StateRepo;
import com.omar.retromp3recorder.app.usecase.ChangeBitrateUC;
import com.omar.retromp3recorder.app.usecase.ChangeSampleRateUC;
import com.omar.retromp3recorder.app.usecase.ShareUC;
import com.omar.retromp3recorder.app.usecase.StartPlaybackUC;
import com.omar.retromp3recorder.app.usecase.StartRecordUC;
import com.omar.retromp3recorder.app.usecase.StopPlaybackAndRecordUC;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

import static com.omar.retromp3recorder.app.main.MainView.BitRateChangeAction;
import static com.omar.retromp3recorder.app.main.MainView.BitrateChangedResult;
import static com.omar.retromp3recorder.app.main.MainView.ErrorLogResult;
import static com.omar.retromp3recorder.app.main.MainView.MainViewAction;
import static com.omar.retromp3recorder.app.main.MainView.MainViewResult;
import static com.omar.retromp3recorder.app.main.MainView.MessageLogResult;
import static com.omar.retromp3recorder.app.main.MainView.PlayAction;
import static com.omar.retromp3recorder.app.main.MainView.RecordAction;
import static com.omar.retromp3recorder.app.main.MainView.SampleRateChangeAction;
import static com.omar.retromp3recorder.app.main.MainView.SampleRateChangeResult;
import static com.omar.retromp3recorder.app.main.MainView.ShareAction;
import static com.omar.retromp3recorder.app.main.MainView.StopAction;
import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

public class MainViewInteractor implements Interactor<MainViewAction, MainViewResult> {

    private final Scheduler scheduler;

    private final ChangeSampleRateUC changeSampleRateUC;
    private final StartRecordUC startRecordUC;
    private final ShareUC shareUC;
    private final StartPlaybackUC startPlaybackUC;
    private final StopPlaybackAndRecordUC stopPlaybackAndRecordUC;
    private final ChangeBitrateUC changeBitrateUC;

    private final BitRateRepo bitRateRepo;
    private final SampleRateRepo sampleRateRepo;
    private final StateRepo stateRepo;
    private final RequestPermissionsRepo requestPermissionsRepo;
    private final LogRepo logRepo;

    @Inject
    public MainViewInteractor(
            Scheduler scheduler,
            ChangeBitrateUC changeBitrateUC,
            ChangeSampleRateUC changeSampleRateUC,
            StartRecordUC startRecordUC, ShareUC shareUC,
            StartPlaybackUC startPlaybackUC,
            StopPlaybackAndRecordUC stopPlaybackAndRecordUC,
            BitRateRepo bitRateRepo,
            SampleRateRepo sampleRateRepo,
            StateRepo stateRepo,
            RequestPermissionsRepo requestPermissionsRepo,
            LogRepo logRepo) {
        this.scheduler = scheduler;
        this.changeBitrateUC = changeBitrateUC;
        this.changeSampleRateUC = changeSampleRateUC;
        this.startRecordUC = startRecordUC;
        this.shareUC = shareUC;
        this.startPlaybackUC = startPlaybackUC;
        this.stopPlaybackAndRecordUC = stopPlaybackAndRecordUC;
        this.bitRateRepo = bitRateRepo;
        this.sampleRateRepo = sampleRateRepo;
        this.stateRepo = stateRepo;
        this.requestPermissionsRepo = requestPermissionsRepo;
        this.logRepo = logRepo;
    }

    @Override
    public ObservableTransformer<MainViewAction, MainViewResult> process() {
        return upstream -> upstream.observeOn(scheduler)
                .compose((ObservableTransformer<MainViewAction, MainViewResult>) actions -> {
                    return Observable.merge(
                            createLinkedList(
                                    actions
                                            .ofType(PlayAction.class)
                                            .flatMapCompletable(playAction -> startPlaybackUC.execute())
                                            .toObservable(),
                                    actions
                                            .ofType(RecordAction.class)
                                            .flatMapCompletable(playAction -> startRecordUC.execute())
                                            .toObservable(),
                                    actions
                                            .ofType(ShareAction.class)
                                            .flatMapCompletable(shareAction -> shareUC.execute())
                                            .toObservable(),
                                    actions
                                            .ofType(StopAction.class)
                                            .flatMapCompletable(stopAction -> stopPlaybackAndRecordUC.execute())
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
                                            .map((Function<VoiceRecorder.SampleRate, MainViewResult>) SampleRateChangeResult::new),
                                    requestPermissionsRepo
                                            .observe()
                                            .ofType(RequestPermissionsRepo.Yes.class)
                                            .map((Function<RequestPermissionsRepo.Yes, MainViewResult>) yes -> {
                                                return new MainView.RequestPermissionsResult(yes.permissions);
                                            }),
                                    logRepo
                                            .observe()
                                            .ofType(LogRepo.Message.class)
                                            .map((Function<LogRepo.Message, MainViewResult>) message -> {
                                                return new MessageLogResult(message.message);
                                            }),
                                    logRepo
                                            .observe()
                                            .ofType(LogRepo.Error.class)
                                            .map((Function<LogRepo.Error, MainViewResult>) message -> {
                                                return new ErrorLogResult(message.error);
                                            }),
                                    stateRepo
                                            .observe()
                                            .map((Function<MainView.State, MainViewResult>) MainView.StateChangedResult::new)

                            ));
                });
    }
}
