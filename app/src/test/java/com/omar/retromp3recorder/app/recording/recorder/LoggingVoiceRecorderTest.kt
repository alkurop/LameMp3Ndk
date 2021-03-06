package com.omar.retromp3recorder.app.recording.recorder

import com.github.alkurop.stringerbell.Stringer.Companion.ofString
import com.omar.retromp3recorder.app.common.repo.LogRepo
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.RecorderProps
import io.reactivex.Scheduler
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Named

class LoggingVoiceRecorderTest {
    @Inject
    lateinit var logRepo: LogRepo

    @Inject
    lateinit var scheduler: Scheduler

    @Inject
    @Named(AppComponent.DECORATOR_A)
    lateinit var baseVoiceRecorder: VoiceRecorder

    @Inject
    @Named(MockModule.RECORDER_SUBJECT)
    lateinit var recorderEvents: Subject<VoiceRecorder.Event>
    private lateinit var spyVoiceRecorder: VoiceRecorder
    private lateinit var loggingVoiceRecorder: LoggingVoiceRecorder

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        spyVoiceRecorder = Mockito.spy(baseVoiceRecorder)
        loggingVoiceRecorder = LoggingVoiceRecorder(spyVoiceRecorder, logRepo, scheduler)
    }

    @Test
    fun test_DidDecorateStartRecord() {
        val recorderProps = RecorderProps(
            "test",
            VoiceRecorder.BitRate._160,
            VoiceRecorder.SampleRate._8000
        )
        loggingVoiceRecorder.record(recorderProps)

        //Then
        Mockito.verify(spyVoiceRecorder).record(recorderProps)
    }

    @Test
    fun test_DidDecorateStopRecord() {
        loggingVoiceRecorder.stopRecord()

        //Then
        Mockito.verify(spyVoiceRecorder).stopRecord()
    }

    @Test
    fun test_OnPlayerMessage_PostLog() {
        recorderEvents.onNext(VoiceRecorder.Event.Message(ofString("test")))

        //Then
        logRepo.observe()
            .test()
            .assertValue { event: LogRepo.Event ->
                ofString(
                    "test"
                )
                    .equals((event as LogRepo.Event.Message).message)
            }
    }

    @Test
    fun test_OnPlayerError_PostLog() {
        recorderEvents.onNext(VoiceRecorder.Event.Error(ofString("test")))

        //Then
        logRepo.observe()
            .test()
            .assertValue { event: LogRepo.Event ->
                ofString(
                    "test"
                )
                    .equals((event as LogRepo.Event.Error).error)
            }
    }

    @Test
    fun test_DidDecorateIsRecording() {
        assert(loggingVoiceRecorder.isRecording())
    }
}