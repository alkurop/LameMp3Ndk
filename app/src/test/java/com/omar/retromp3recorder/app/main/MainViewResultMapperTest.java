package com.omar.retromp3recorder.app.main;

import com.omar.retromp3recorder.app.recorder.VoiceRecorder;

import org.junit.Before;
import org.junit.Test;

import io.reactivex.observers.TestObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

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
    }

    @Test
    public void test_StateChangedResult_Mapped() {
    }

    @Test
    public void test_RequestPermissionsResult_Mapped() {
    }

    @Test
    public void test_PlayerIdResult_Mapped() {
    }

    @Test
    public void test_UnknownResult_crash() {
    }

    @Test
    public void test_State_Reducer_Combines_ViewModels() {
    }
}