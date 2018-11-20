package com.omar.retromp3recorder.app.main;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.recording.repo.BitRateRepo;
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo;
import com.omar.retromp3recorder.app.common.repo.StateRepo;
import com.omar.retromp3recorder.app.recording.usecase.CheckPermissionsUC;
import com.omar.retromp3recorder.app.share.ShareUC;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MainViewInteractorActionsTest {

    @Inject
    MainViewInteractor interactor;
    @Inject
    BitRateRepo bitRateRepo;
    @Inject
    SampleRateRepo sampleRateRepo;
    @Inject
    ShareUC shareUC;
    @Inject
    StateRepo stateRepo;

    @Inject
    CheckPermissionsUC permissionsUC;

    @Inject
    RequestPermissionsRepo requestPermissionsRepo;

    private final Subject<MainView.Action> actionSubject = PublishSubject.create();
    private TestObserver<MainView.Result> test;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        when(permissionsUC.execute(any())).thenAnswer(invocation -> {
            requestPermissionsRepo.newValue(new RequestPermissionsRepo.Granted());
            return Completable.complete();
        });
        test = actionSubject.compose(interactor.process()).test();
    }

    @Test
    public void test_ChangeBitrate_Action_UC_Executed() {
        VoiceRecorder.BitRate bitRate = VoiceRecorder.BitRate._160;

        //When
        actionSubject.onNext(new MainView.BitRateChangeAction(
                bitRate
        ));

        //Then
        bitRateRepo.observe().test().assertValue(bitRate);
    }

    @Test
    public void test_ChangeSampleRate_Action_UC_Executed() {
        VoiceRecorder.SampleRate sampleRate = VoiceRecorder.SampleRate._11025;

        //When
        actionSubject.onNext(new MainView.SampleRateChangeAction(
                sampleRate
        ));

        //Then
        sampleRateRepo.observe().test().assertValue(sampleRate);
    }


    @Test
    public void test_Share_Action_UC_Executed() {
        MainView.ShareAction shareAction = new MainView.ShareAction();

        //When
        actionSubject.onNext(shareAction);

        //Then
        verify(shareUC).execute();
    }

    @Test
    public void test_StartPlayback_Action_UC_Executed() {
        MainView.PlayAction action = new MainView.PlayAction();

        //When
        actionSubject.onNext(action);

        //then
        stateRepo.observe().test().assertValue(MainView.State.Playing);

    }

    @Test
    public void test_StopPlaybackAndRecord_Action_UC_Executed() {
        MainView.StopAction action = new MainView.StopAction();

        //When
        actionSubject.onNext(action);

        //then
        stateRepo.observe().test().assertValue(MainView.State.Idle);
    }

    @Test
    public void test_StartRecord_Action_UC_Executed() {
        MainView.RecordAction action = new MainView.RecordAction();

        //When
        actionSubject.onNext(action);

        //then
        stateRepo.observe().test().assertValue(MainView.State.Recording);
    }
}