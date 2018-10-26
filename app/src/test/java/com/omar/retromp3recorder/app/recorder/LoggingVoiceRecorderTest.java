package com.omar.retromp3recorder.app.recorder;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.repo.LogRepo;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.subjects.Subject;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_ALPHA;
import static com.omar.retromp3recorder.app.di.MockModule.RECORDER_SUBJECT;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class LoggingVoiceRecorderTest {

    @Inject
    LogRepo logRepo;
    @Inject
    Scheduler scheduler;
    @Inject
    @Named(DECORATOR_ALPHA)
    VoiceRecorder baseVoiceRecorder;
    @Inject
    @Named(RECORDER_SUBJECT)
    Subject<VoiceRecorder.Event> recorderEvents;

    private VoiceRecorder spyVoiceRecoreder;

    private LoggingVoiceRecorder loggingVoiceRecorder;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        spyVoiceRecoreder = spy(baseVoiceRecorder);
        loggingVoiceRecorder = new LoggingVoiceRecorder(spyVoiceRecoreder, logRepo, scheduler);
    }

    @Test
    public void test_DidDecorateStartRecord() {
        VoiceRecorder.RecorderProps recorderProps = new VoiceRecorder.RecorderProps(
                "test",
                VoiceRecorder.BitRate._160,
                VoiceRecorder.SampleRate._8000);
        loggingVoiceRecorder.record(recorderProps);

        verify(spyVoiceRecoreder).record(recorderProps);
    }

    @Test
    public void test_DidDecorateStopRecord() {
        loggingVoiceRecorder.stopRecord();

        verify(spyVoiceRecoreder).stopRecord();
    }

    @Test
    public void test_OnPlayerMessage_PostLog() {
        recorderEvents.onNext(new VoiceRecorder.Message("test"));

        logRepo.observe()
                .test()
                .assertValue(event ->
                        "test".equals(((LogRepo.Message) event).message)
                );
    }

    @Test
    public void test_OnPlayerError_PostLog() {
        recorderEvents.onNext(new VoiceRecorder.Error("test"));

        logRepo.observe()
                .test()
                .assertValue(event ->
                        "test".equals(((LogRepo.Error) event).error)
                );
    }

    @Test
    public void test_DidDecorateIsRecording(){
        assert loggingVoiceRecorder.isRecording();
    }

}