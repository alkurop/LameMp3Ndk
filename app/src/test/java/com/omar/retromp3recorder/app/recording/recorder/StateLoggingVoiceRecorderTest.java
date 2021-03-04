package com.omar.retromp3recorder.app.recording.recorder;

import com.github.alkurop.stringerbell.Stringer;
import com.omar.retromp3recorder.app.common.repo.StateRepo;
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.main.MainView;

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

public class StateLoggingVoiceRecorderTest {


    @Inject
    StateRepo stateRepo;
    @Inject
    Scheduler scheduler;
    @Inject
    @Named(DECORATOR_A)
    VoiceRecorder baseVoiceRecorder;
    @Inject
    @Named(RECORDER_SUBJECT)
    Subject<VoiceRecorder.Event> recorderEvents;
    private VoiceRecorder spy;
    private StateLoggingVoiceRecorder stateLoggingVoiceRecorder;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        spy = spy(baseVoiceRecorder);
        stateLoggingVoiceRecorder = new StateLoggingVoiceRecorder(spy, scheduler, stateRepo);
    }

    @Test
    public void test_DidDecorateStartPlay() {
        VoiceRecorder.RecorderProps recorderProps = new VoiceRecorder.RecorderProps(
                "test",
                VoiceRecorder.BitRate._160,
                VoiceRecorder.SampleRate._8000);

        //When
        stateLoggingVoiceRecorder.record(recorderProps);

        //Then
        verify(spy).record(recorderProps);
    }

    @Test
    public void test_DidDecorateStopPlay() {
        stateLoggingVoiceRecorder.stopRecord();

        //Then
        verify(spy).stopRecord();
    }

    @Test
    public void test_OnStartRecord_PostState() {
        VoiceRecorder.RecorderProps recorderProps = new VoiceRecorder.RecorderProps(
                "test",
                VoiceRecorder.BitRate._160,
                VoiceRecorder.SampleRate._8000);

        //When
        stateLoggingVoiceRecorder.record(recorderProps);

        //Then
        stateRepo.observe()
                .test()
                .assertValue(state -> state == MainView.State.Recording);
    }

    @Test
    public void test_OnStopRecordPostState(){
        stateLoggingVoiceRecorder.stopRecord();

        //Then
        stateRepo.observe()
                .test()
                .assertValue(state -> state == MainView.State.Idle);
    }

    @Test
    public void test_OnRecorderError_PostState() {
        recorderEvents.onNext(new VoiceRecorder.Event.Error(Stringer.Companion.ofString("test")));

        //Then
        stateRepo.observe()
                .test()
                .assertValue(state -> state == MainView.State.Idle);
    }

    @Test
    public void test_DidDecorateIsRecording(){
        assert stateLoggingVoiceRecorder.isRecording();
    }
}