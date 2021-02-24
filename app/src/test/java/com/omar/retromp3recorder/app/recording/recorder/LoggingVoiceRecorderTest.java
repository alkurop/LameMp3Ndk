package com.omar.retromp3recorder.app.recording.recorder;

import com.omar.retromp3recorder.app.common.repo.LogRepo;
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.subjects.Subject;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_A;
import static com.omar.retromp3recorder.app.di.MockModule.RECORDER_SUBJECT;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class LoggingVoiceRecorderTest {

    @Inject
    LogRepo logRepo;
    @Inject
    Scheduler scheduler;
    @Inject
    @Named(DECORATOR_A)
    VoiceRecorder baseVoiceRecorder;
    @Inject
    @Named(RECORDER_SUBJECT)
    Subject<VoiceRecorder.Event> recorderEvents;

    private VoiceRecorder spyVoiceRecorder;

    private LoggingVoiceRecorder loggingVoiceRecorder;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        spyVoiceRecorder = spy(baseVoiceRecorder);
        loggingVoiceRecorder = new LoggingVoiceRecorder(spyVoiceRecorder, logRepo, scheduler);
    }

    @Test
    public void test_DidDecorateStartRecord() {
        VoiceRecorder.RecorderProps recorderProps = new VoiceRecorder.RecorderProps(
                "test",
                VoiceRecorder.BitRate._160,
                VoiceRecorder.SampleRate._8000);
        loggingVoiceRecorder.record(recorderProps);

        //Then
        verify(spyVoiceRecorder).record(recorderProps);
    }

    @Test
    public void test_DidDecorateStopRecord() {
        loggingVoiceRecorder.stopRecord();

        //Then
        verify(spyVoiceRecorder).stopRecord();
    }

    @Test
    public void test_OnPlayerMessage_PostLog() {
        recorderEvents.onNext(new VoiceRecorder.Message("test"));

        //Then
        logRepo.observe()
                .test()
                .assertValue(event ->
                        "test".equals(((LogRepo.Event.Message) event).getMessage())
                );
    }

    @Test
    public void test_OnPlayerError_PostLog() {
        recorderEvents.onNext(new VoiceRecorder.Error("test"));

        //Then
        logRepo.observe()
                .test()
                .assertValue(event ->
                        "test".equals(((LogRepo.Event.Error) event).getError())
                );
    }

    @Test
    public void test_DidDecorateIsRecording(){
        assert loggingVoiceRecorder.isRecording();
    }

}