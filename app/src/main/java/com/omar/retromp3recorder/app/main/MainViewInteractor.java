package com.omar.retromp3recorder.app.main;


import com.omar.retromp3recorder.app.mvi.Interactor;
import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.repo.BitRateRepo;
import com.omar.retromp3recorder.app.repo.LogRepo;
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

public class MainViewInteractor implements Interactor<Action, Result> {

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

    private final AudioPlayer audioPlayer;
    private final VoiceRecorder voiceRecorder;

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
            LogRepo logRepo, AudioPlayer audioPlayer, VoiceRecorder voiceRecorder) {
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
        this.audioPlayer = audioPlayer;
        this.voiceRecorder = voiceRecorder;
    }

    @Override
    public ObservableTransformer<Action, Result> process() {
        return upstream -> upstream
                .observeOn(scheduler)
                .compose((ObservableTransformer<Action, Result>) actions -> {
                    return Observable.merge(
                            createLinkedList(
                                    actionsMapper(actions).toObservable(),
                                    repoMapper(),
                                    audioPlayerMapper(),
                                    voiceRecorderMapper()
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
                        .ofType(RequestPermissionsRepo.Yes.class)
                        .map((Function<RequestPermissionsRepo.Yes, Result>) yes ->
                                new MainView.RequestPermissionsResult(yes.permissions)
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
                        )
        ));
    }

    private Observable<MainView.Result> audioPlayerMapper() {
        return Observable.merge(createLinkedList(
                audioPlayer.observeEvents()
                        .ofType(AudioPlayer.Error.class)
                        .map((Function<AudioPlayer.Error, Result>) error ->
                                new MainView.StateChangedResult(MainView.State.Idle)
                        ),
                audioPlayer.observeEvents()
                        .ofType(AudioPlayer.PlaybackEnded.class)
                        .map((Function<AudioPlayer.PlaybackEnded, Result>) error ->
                                new MainView.StateChangedResult(MainView.State.Idle)
                        ),
                audioPlayer.observeEvents()
                        .ofType(AudioPlayer.SendPlayerId.class)
                        .map((Function<AudioPlayer.SendPlayerId, Result>) id ->
                                new MainView.SetPlayerId(id.playerId)
                        )
        ));
    }

    private Observable<MainView.Result> voiceRecorderMapper() {
        return Observable.merge(createLinkedList(
                voiceRecorder.observeEvents().ofType(VoiceRecorder.Error.class)
                        .map((Function<VoiceRecorder.Error, Result>) error ->
                                new MainView.StateChangedResult(MainView.State.Idle)
                        )
        ));
    }
}
