package com.omar.retromp3recorder.app.main;

import com.omar.retromp3recorder.app.recorder.VoiceRecorder;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static com.omar.retromp3recorder.app.utils.VarargHelper.createHashSet;

public class MainViewResultMapperTest {

    private final Subject<MainView.Result> resultPublisher = PublishSubject.create();
    private TestObserver<MainView.MainViewModel> test;

    @Before
    public void setUp() {
        test = resultPublisher.compose(MainViewResultMapper.map())
                .test();
    }

    @Test
    public void test_MapperStartsWithDefaultViewModel() {
        test.assertNoErrors()
                .assertNotComplete()
                .assertNoErrors()
                .assertValueCount(1);
    }

    @Test
    public void test_MessageLogResult_Mapped() {
        final MainView.MessageLogResult messageLogResult =
                new MainView.MessageLogResult("test");

        //When
        resultPublisher.onNext(messageLogResult);

        //Then
        test.assertNoErrors()
                .assertNotComplete()
                .assertNoErrors()
                .assertValueCount(2)
                .assertValueAt(1, mainViewModel ->
                        "test".equalsIgnoreCase(mainViewModel.message))
        ;
    }

    @Test
    public void test_ErrorLogResult_Mapped() {
        MainView.ErrorLogResult errorLogResult =
                new MainView.ErrorLogResult("test");

        //When
        resultPublisher.onNext(errorLogResult);

        //Then
        test.assertNoErrors()
                .assertNotComplete()
                .assertNoErrors()
                .assertValueCount(2)
                .assertValueAt(1, mainViewModel ->
                        "test".equalsIgnoreCase(mainViewModel.error))
        ;
    }

    @Test
    public void test_BitrateChangedResult_Mapped() {
        MainView.BitrateChangedResult bitrateChangedResult
                = new MainView.BitrateChangedResult(VoiceRecorder.BitRate._128);
        //When
        resultPublisher.onNext(bitrateChangedResult);

        //Then
        test.assertNoErrors()
                .assertNotComplete()
                .assertNoErrors()
                .assertValueCount(2)
                .assertValueAt(1, mainViewModel ->
                        mainViewModel.bitRate == VoiceRecorder.BitRate._128)
        ;
    }

    @Test
    public void test_SampleRateChangeResult_Mapped() {
        MainView.SampleRateChangeResult bitrateChangedResult
                = new MainView.SampleRateChangeResult(VoiceRecorder.SampleRate._22050);

        //When
        resultPublisher.onNext(bitrateChangedResult);

        //Then
        test.assertNoErrors()
                .assertNotComplete()
                .assertNoErrors()
                .assertValueCount(2)
                .assertValueAt(1, mainViewModel ->
                        mainViewModel.sampleRate == VoiceRecorder.SampleRate._22050)
        ;
    }

    @Test
    public void test_StateChangedResult_Mapped() {
        MainView.StateChangedResult stateChangedResult =
                new MainView.StateChangedResult(MainView.State.Playing);

        //When
        resultPublisher.onNext(stateChangedResult);

        //Then
        test.assertNoErrors()
                .assertNotComplete()
                .assertNoErrors()
                .assertValueCount(2)
                .assertValueAt(1, mainViewModel ->
                        mainViewModel.state == MainView.State.Playing)
        ;
    }

    @Test
    public void test_RequestPermissionsResult_Mapped() {
        HashSet<String> permissionsToRequest = createHashSet("test");
        MainView.RequestPermissionsResult requestPermissionsResult =
                new MainView.RequestPermissionsResult(permissionsToRequest);

        //When
        resultPublisher.onNext(requestPermissionsResult);

        //Then
        test.assertNoErrors()
                .assertNotComplete()
                .assertNoErrors()
                .assertValueCount(2)
                .assertValueAt(1, mainViewModel ->
                        mainViewModel.requestForPermissions == permissionsToRequest)
        ;
    }

    @Test
    public void test_PlayerIdResult_Mapped() {
        int playerId = 28;
        MainView.PlayerIdResult playerIdResult = new MainView.PlayerIdResult(playerId);

        //When
        resultPublisher.onNext(playerIdResult);

        //Then
        test.assertNoErrors()
                .assertNotComplete()
                .assertNoErrors()
                .assertValueCount(2)
                .assertValueAt(1, mainViewModel ->
                        mainViewModel.playerId == playerId)
        ;
    }

    @Test
    public void test_UnknownResult_crash() {
        class UnknownResult implements MainView.Result{}

        //When
        resultPublisher.onNext(new UnknownResult());

        //Then
        test.assertError(IllegalStateException.class);
    }
}