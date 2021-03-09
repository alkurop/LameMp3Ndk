package com.omar.retromp3recorder.app.playback.player

import com.github.alkurop.stringerbell.Stringer.Companion.ofString
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.di.MockModule
import io.reactivex.Scheduler
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Named

class LoggingAudioPlayerTest {
    @Inject
    lateinit var logRepo: LogRepo

    @Inject
    lateinit var scheduler: Scheduler

    @Inject
    @Named(AppComponent.DECORATOR_A)
    lateinit var baseAudioPlayer: AudioPlayer

    @Inject
    @Named(MockModule.PLAYER_SUBJECT)
    lateinit var audioEvents: Subject<AudioPlayer.Event>
    private lateinit var spyAudioPlayer: AudioPlayer
    private lateinit var loggingAudioPlayer: LoggingAudioPlayer

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        spyAudioPlayer = Mockito.spy(baseAudioPlayer)
        loggingAudioPlayer = LoggingAudioPlayer(spyAudioPlayer, scheduler, logRepo)
    }

    @Test
    fun test_DidDecorateStartPlay() {
        loggingAudioPlayer.playerStart("test")

        //Then
        Mockito.verify(spyAudioPlayer).playerStart("test")
    }

    @Test
    fun test_DidDecorateStopPlay() {
        loggingAudioPlayer.playerStop()

        //Then
        Mockito.verify(spyAudioPlayer).playerStop()
    }

    @Test
    fun test_OnPlayerMessage_PostLog() {
        audioEvents.onNext(AudioPlayer.Event.Message(ofString("test")))

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
        audioEvents.onNext(AudioPlayer.Event.Error(ofString("test")))

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
    fun test_DidDecorateIsPlaying() {
        assert(loggingAudioPlayer.isPlaying)
    }
}