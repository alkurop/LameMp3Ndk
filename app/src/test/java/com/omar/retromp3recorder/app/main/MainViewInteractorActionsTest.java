package com.omar.retromp3recorder.app.main;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.usecase.ChangeBitrateUC;
import com.omar.retromp3recorder.app.usecase.ChangeSampleRateUC;
import com.omar.retromp3recorder.app.usecase.ShareUC;
import com.omar.retromp3recorder.app.usecase.StartPlaybackUC;
import com.omar.retromp3recorder.app.usecase.StartRecordUC;
import com.omar.retromp3recorder.app.usecase.StopPlaybackAndRecordUC;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static org.mockito.Mockito.verify;

public class MainViewInteractorActionsTest {

    @Inject
    MainViewInteractor interactor;
    @Inject
    ChangeSampleRateUC changeSampleRateUC;
    @Inject
    ChangeBitrateUC changeBitrateUC;
    @Inject
    ShareUC shareUC;
    @Inject
    StartPlaybackUC startPlaybackUC;
    @Inject
    StartRecordUC startRecordUC;
    @Inject
    StopPlaybackAndRecordUC stopPlaybackAndRecordUC;

    private final Subject<MainView.Action> actionSubject = PublishSubject.create();
    private TestObserver<MainView.Result> test;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
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
        verify(changeBitrateUC).execute(bitRate);
    }

    @Test
    public void test_ChangeSampleRate_Action_UC_Executed() {
        VoiceRecorder.SampleRate sampleRate = VoiceRecorder.SampleRate._11025;

        //When
        actionSubject.onNext(new MainView.SampleRateChangeAction(
                sampleRate
        ));

        //Then
        verify(changeSampleRateUC).execute(sampleRate);
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
        verify(startPlaybackUC).execute();

    }

    @Test
    public void test_StopPlaybackAndRecord_Action_UC_Executed() {
        MainView.StopAction action = new MainView.StopAction();

        //When
        actionSubject.onNext(action);

        //then
        verify(stopPlaybackAndRecordUC).execute();
    }

    @Test
    public void test_StartRecord_Action_UC_Executed() {
        MainView.RecordAction action = new MainView.RecordAction();

        //When
        actionSubject.onNext(action);

        //then
        verify(startRecordUC).execute();
    }
}