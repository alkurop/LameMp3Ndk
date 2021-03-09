package com.omar.retromp3recorder.app.playback.player

import com.github.alkurop.ghostinshell.Shell
import com.github.alkurop.stringerbell.Stringer.Companion.ofString
import com.omar.retromp3recorder.app.common.repo.StateRepo
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.app.main.MainView
import com.omar.retromp3recorder.app.playback.player.AudioPlayer.Event.SendPlayerId
import com.omar.retromp3recorder.app.playback.repo.PlayerIdRepo
import io.reactivex.Scheduler
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Named

class StateLoggingAudioPlayerTest {
    @Inject
    lateinit var stateRepo: StateRepo

    @Inject
    lateinit var playerIdRepo: PlayerIdRepo

    @Inject
    lateinit var scheduler: Scheduler

    @Inject
    @Named(AppComponent.DECORATOR_A)
    lateinit var baseAudioPlayer: AudioPlayer

    @Inject
    @Named(MockModule.PLAYER_SUBJECT)
    lateinit var audioEvents: Subject<AudioPlayer.Event>
    private lateinit var spy: AudioPlayer
    private lateinit var stateLoggingAudioPlayer: StateLoggingAudioPlayer

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        spy = Mockito.spy(baseAudioPlayer)
        stateLoggingAudioPlayer = StateLoggingAudioPlayer(
            stateRepo, spy,
            playerIdRepo, scheduler
        )
    }

    @Test
    fun test_DidDecorateStartPlay() {
        stateLoggingAudioPlayer.playerStart("test")

        //Then
        Mockito.verify(spy).playerStart("test")
    }

    @Test
    fun test_DidDecorateStopPlay() {
        stateLoggingAudioPlayer.playerStop()

        //Then
        Mockito.verify(spy).playerStop()
    }

    @Test
    fun test_OnStartPlay_PostState() {
        stateLoggingAudioPlayer.playerStart("test")

        //Then
        stateRepo.observe()
            .test()
            .assertValue { state: MainView.State -> state === MainView.State.Playing }
    }

    @Test
    fun test_OnStopPlayPostState() {
        stateLoggingAudioPlayer.playerStop()

        //Then
        stateRepo.observe()
            .test()
            .assertValue { state: MainView.State -> state === MainView.State.Idle }
    }

    @Test
    fun test_PlayerId_PostToRepo() {
        audioEvents.onNext(SendPlayerId(10))

        //Then
        playerIdRepo.observe().test()
            .assertValue { integerOneShot: Shell<Int> -> integerOneShot.ghost == 10 }
    }

    @Test
    fun test_OnPlayerError_PostIdleState() {
        audioEvents.onNext(AudioPlayer.Event.Error(ofString("test")))

        //Then
        stateRepo.observe().test()
            .assertValue { state: MainView.State -> state === MainView.State.Idle }
    }

    @Test
    fun test_DidDecorateIsPlaying() {
        assert(stateLoggingAudioPlayer.isPlaying)
    }
}