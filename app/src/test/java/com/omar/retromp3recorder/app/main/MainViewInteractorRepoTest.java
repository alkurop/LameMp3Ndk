package com.omar.retromp3recorder.app.main;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.repo.BitRateRepo;
import com.omar.retromp3recorder.app.repo.LogRepo;
import com.omar.retromp3recorder.app.repo.PlayerIdRepo;
import com.omar.retromp3recorder.app.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.repo.SampleRateRepo;
import com.omar.retromp3recorder.app.repo.StateRepo;

import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import javax.inject.Inject;

import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;

import static com.omar.retromp3recorder.app.recorder.VoiceRecorder.SampleRate._8000;
import static com.omar.retromp3recorder.app.utils.VarargHelper.createHashSet;

public class MainViewInteractorRepoTest {

    private static final int FIRST_EVENT_INDEX = 3;

    @Inject MainViewInteractor interactor;
    @Inject BitRateRepo bitRateRepo;
    @Inject LogRepo logRepo;
    @Inject PlayerIdRepo playerIdRepo;
    @Inject RequestPermissionsRepo requestPermissionsRepo;
    @Inject StateRepo stateRepo;
    @Inject SampleRateRepo sampleRateRepo;

    private TestObserver<MainView.Result> test;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        test = PublishSubject.<MainView.Action>create()
                .compose(interactor.process())
                .test();
    }

    @Test
    public void test_Listening_Default_Config_Values(){
        test.assertValueCount(FIRST_EVENT_INDEX);
    }

    @Test
    public void test_Listening_BitRateRepo() {
        //When
        bitRateRepo.newValue(VoiceRecorder.BitRate._128);

        //Then
        test.assertValueAt(FIRST_EVENT_INDEX,  result -> ((
                MainView.BitrateChangedResult) result).bitRate == VoiceRecorder.BitRate._128
        );
    }

    @Test
    public void test_Listening_LogRepo() {
        //When
        logRepo.newValue(new LogRepo.Message("test"));

        //Then
        test.assertValueAt(FIRST_EVENT_INDEX, result -> {
                    String message = ((MainView.MessageLogResult) result).message;
                    return "test".equals(message);
                }
        );
    }

    @Test
    public void test_Listening_PlayerIdRepo() {
        //When
        playerIdRepo.newValue(38);

        //Then
        test.assertValueAt(FIRST_EVENT_INDEX, result -> {
                    int playerId = ((MainView.PlayerIdResult) result).playerId;
                    return playerId == 38;
                }
        );
    }

    @Test
    public void test_Listening_RequestPermissionsRepo() {
        RequestPermissionsRepo.ShouldRequestPermissions shouldRequestPermissions
                = new RequestPermissionsRepo.Yes(createHashSet("test"));

        //When
        requestPermissionsRepo.newValue(shouldRequestPermissions);

        //Then
        test.assertValueAt(FIRST_EVENT_INDEX, result -> {
            MainView.RequestPermissionsResult requestPermissionsResult =
                    (MainView.RequestPermissionsResult) result;
            Set<String> permissions = requestPermissionsResult.permissionsToRequest;
            return permissions.size() == 1 && permissions.contains("test");
        });
    }

    @Test
    public void test_Listening_StateRepo() {
        stateRepo.newValue(MainView.State.Recording);

        //Then
        test.assertValueAt(FIRST_EVENT_INDEX, result -> {
            MainView.StateChangedResult stateChangedResult =
                    (MainView.StateChangedResult) result;
            MainView.State state = stateChangedResult.state;
            return state == MainView.State.Recording;
        });
    }

    @Test
    public void test_Listening_SampleRateRepo() {
        sampleRateRepo.newValue(_8000);

        //Then
        test.assertValueAt(FIRST_EVENT_INDEX, result -> {
            MainView.SampleRateChangeResult stateChangedResult =
                    (MainView.SampleRateChangeResult) result;
           VoiceRecorder.SampleRate sampleRate = stateChangedResult.sampleRate;
            return sampleRate == _8000;
        });
    }
}