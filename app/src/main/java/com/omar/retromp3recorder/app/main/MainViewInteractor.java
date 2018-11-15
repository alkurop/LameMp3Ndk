package com.omar.retromp3recorder.app.main;


import com.omar.retromp3recorder.app.playback.repo.PlayerIdRepo;
import com.omar.retromp3recorder.app.playback.usecase.StartPlaybackUC;
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.recording.repo.BitRateRepo;
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo;
import com.omar.retromp3recorder.app.recording.usecase.ChangeBitrateUC;
import com.omar.retromp3recorder.app.recording.usecase.ChangeSampleRateUC;
import com.omar.retromp3recorder.app.recording.usecase.StartRecordUC;
import com.omar.retromp3recorder.app.shared.repo.LogRepo;
import com.omar.retromp3recorder.app.shared.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.shared.repo.StateRepo;
import com.omar.retromp3recorder.app.share.ShareUC;
import com.omar.retromp3recorder.app.shared.usecase.StopPlaybackAndRecordUC;
import com.omar.retromp3recorder.app.utils.OneShot;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;

import static com.omar.retromp3recorder.app.main.MainView.Action;
import static com.omar.retromp3recorder.app.main.MainView.BitRateChangeAction;
import static com.omar.retromp3recorder.app.main.MainView.BitrateChangedResult;
import static com.omar.retromp3recorder.app.main.MainView.ErrorLogResult;
import static com.omar.retromp3recorder.app.main.MainView.MessageLogResult;
import static com.omar.retromp3recorder.app.main.MainView.PlayAction;
import static com.omar.retromp3recorder.app.main.MainView.RecordAction;
import static com.omar.retromp3recorder.app.main.MainView.Result;
import static com.omar.retromp3recorder.app.main.MainView.SampleRateChangeAction;
import static com.omar.retromp3recorder.app.main.MainView.SampleRateChangeResult;
import static com.omar.retromp3recorder.app.main.MainView.ShareAction;
import static com.omar.retromp3recorder.app.main.MainView.StopAction;
import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

public class MainViewInteractor {

    //region fields
    private final Scheduler scheduler;

    private final ChangeBitrateUC changeBitrateUC;
    private final ChangeSampleRateUC changeSampleRateUC;
    private final ShareUC shareUC;
    private final StartPlaybackUC startPlaybackUC;
    private final StartRecordUC startRecordUC;
    private final StopPlaybackAndRecordUC stopPlaybackAndRecordUC;

    private final BitRateRepo bitRateRepo;
    private final LogRepo logRepo;
    private final PlayerIdRepo playerIdRepo;
    private final RequestPermissionsRepo requestPermissionsRepo;
    private final StateRepo stateRepo;
    private final SampleRateRepo sampleRateRepo;

    //endregion

    //region constructor
    @Inject
    public MainViewInteractor(
            Scheduler scheduler,
            ChangeBitrateUC changeBitrateUC,
            ChangeSampleRateUC changeSampleRateUC,
            StartRecordUC startRecordUC,
            ShareUC shareUC,
            StartPlaybackUC startPlaybackUC,
            StopPlaybackAndRecordUC stopPlaybackAndRecordUC,
            BitRateRepo bitRateRepo,
            SampleRateRepo sampleRateRepo,
            StateRepo stateRepo,
            RequestPermissionsRepo requestPermissionsRepo,
            LogRepo logRepo,
            PlayerIdRepo playerIdRepo) {
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
        this.playerIdRepo = playerIdRepo;
    }
    //endregion

    ObservableTransformer<Action, Result> process() {
        return upstream -> upstream
                .observeOn(scheduler)
                .compose((ObservableTransformer<Action, Result>) actions -> {
                    return Observable.merge(
                            createLinkedList(
                                    actionsMapper(actions)
                                            .toObservable(),
                                    repoMapper()
                            ));
                });
    }

    private Completable actionsMapper(Observable<MainView.Action> actions) {
        return Completable.merge(
                createLinkedList(
                        actions
                                .ofType(PlayAction.class)
                                .flatMapCompletable(playAction ->
                                        startPlaybackUC.execute()
                                ),
                        actions
                                .ofType(RecordAction.class)
                                .flatMapCompletable(playAction ->
                                        startRecordUC.execute()
                                ),
                        actions
                                .ofType(ShareAction.class)
                                .flatMapCompletable(shareAction ->
                                        shareUC.execute()
                                ),
                        actions
                                .ofType(StopAction.class)
                                .flatMapCompletable(stopAction ->
                                        stopPlaybackAndRecordUC.execute()
                                ),
                        actions
                                .ofType(SampleRateChangeAction.class)
                                .flatMapCompletable(sampleRateChangeAction ->
                                        changeSampleRateUC.
                                                execute(sampleRateChangeAction.sampleRate)
                                ),
                        actions
                                .ofType(BitRateChangeAction.class)
                                .flatMapCompletable(bitRateChangeAction ->
                                        changeBitrateUC.execute(bitRateChangeAction.bitRate)
                                )
                )

        );
    }

    private Observable<MainView.Result> repoMapper() {
        return Observable.merge(createLinkedList(
                bitRateRepo
                        .observe()
                        .map((Function<VoiceRecorder.BitRate, Result>)
                                BitrateChangedResult::new
                        ),
                sampleRateRepo
                        .observe()
                        .map((Function<VoiceRecorder.SampleRate, Result>)
                                SampleRateChangeResult::new
                        ),
                requestPermissionsRepo
                        .observe()
                        .filter(shouldRequestPermissionsOneShot -> !shouldRequestPermissionsOneShot.isShot())
                        .map(OneShot::getValueOnce)
                        .ofType(RequestPermissionsRepo.Denied.class)
                        .map((Function<RequestPermissionsRepo.Denied, Result>) denied ->
                                new MainView.RequestPermissionsResult(denied.permissions)
                        ),
                logRepo
                        .observe()
                        .ofType(LogRepo.Message.class)
                        .map((Function<LogRepo.Message, Result>) message ->
                                new MessageLogResult(message.message)
                        ),
                logRepo
                        .observe()
                        .ofType(LogRepo.Error.class)
                        .map((Function<LogRepo.Error, Result>) message ->
                                new ErrorLogResult(message.error)
                        ),
                stateRepo
                        .observe()
                        .map((Function<MainView.State, Result>)
                                MainView.StateChangedResult::new
                        ),
                playerIdRepo
                        .observe()
                        .filter(integerOneShot -> !integerOneShot.isShot())
                        .map(OneShot::getValueOnce)
                        .map(MainView.PlayerIdResult::new)
        ));
    }
}
