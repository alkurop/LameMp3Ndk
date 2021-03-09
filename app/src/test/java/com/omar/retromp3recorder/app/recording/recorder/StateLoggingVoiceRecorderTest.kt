package com.omar.retromp3recorder.app.recording.recorder

import com.github.alkurop.stringerbell.Stringer.Companion.ofString
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.app.main.MainView
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.RecorderProps
import io.reactivex.Scheduler
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Named

class StateLoggingVoiceRecorderTest {

    @Inject
    lateinit var scheduler: Scheduler

    @Inject
    @Named(AppComponent.DECORATOR_A)
    lateinit var baseVoiceRecorder: VoiceRecorder

    @Inject
    @Named(MockModule.RECORDER_SUBJECT)
    lateinit var recorderEvents: Subject<VoiceRecorder.Event>
    private lateinit var spy: VoiceRecorder
    private lateinit var stateLoggingVoiceRecorder: StateLoggingVoiceRecorder

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        spy = Mockito.spy(baseVoiceRecorder)
        stateLoggingVoiceRecorder = StateLoggingVoiceRecorder(spy, scheduler)
    }

    @Test
    fun test_DidDecorateStartPlay() {
        val recorderProps = RecorderProps(
            "test",
            VoiceRecorder.BitRate._160,
            VoiceRecorder.SampleRate._8000
        )

        //When
        stateLoggingVoiceRecorder.record(recorderProps)

        //Then
        Mockito.verify(spy).record(recorderProps)
    }

    @Test
    fun test_DidDecorateStopPlay() {
        stateLoggingVoiceRecorder.stopRecord()

        //Then
        Mockito.verify(spy).stopRecord()
    }

    @Test
    fun test_OnStartRecord_PostState() {
        val recorderProps = RecorderProps(
            "test",
            VoiceRecorder.BitRate._160,
            VoiceRecorder.SampleRate._8000
        )

        //When
        stateLoggingVoiceRecorder.record(recorderProps)

        //Then
        stateRepo.observe()
            .test()
            .assertValue { state: MainView.State -> state === MainView.State.Recording }
    }

    @Test
    fun test_OnStopRecordPostState() {
        stateLoggingVoiceRecorder.stopRecord()

        //Then
        stateRepo.observe()
            .test()
            .assertValue { state: MainView.State -> state === MainView.State.Idle }
    }

    @Test
    fun test_OnRecorderError_PostState() {
        recorderEvents.onNext(VoiceRecorder.Event.Error(ofString("test")))

        //Then
        stateRepo.observe()
            .test()
            .assertValue { state: MainView.State -> state === MainView.State.Idle }
    }

    @Test
    fun test_DidDecorateIsRecording() {
        assert(stateLoggingVoiceRecorder.isRecording())
    }
}