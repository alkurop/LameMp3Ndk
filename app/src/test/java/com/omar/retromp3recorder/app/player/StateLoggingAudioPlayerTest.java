package com.omar.retromp3recorder.app.player;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.main.MainView;
import com.omar.retromp3recorder.app.repo.PlayerIdRepo;
import com.omar.retromp3recorder.app.repo.StateRepo;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.subjects.Subject;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_ALPHA;
import static com.omar.retromp3recorder.app.di.MockModule.PLAYER_SUBJECT;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class StateLoggingAudioPlayerTest {


    @Inject
    StateRepo stateRepo;
    @Inject
    PlayerIdRepo playerIdRepo;
    @Inject
    Scheduler scheduler;
    @Inject
    @Named(DECORATOR_ALPHA)
    AudioPlayer baseAudioPlayer;
    @Inject
    @Named(PLAYER_SUBJECT)
    Subject<AudioPlayer.Event> audioEvents;
    private AudioPlayer spy;
    private StateLoggingAudioPlayer stateLoggingAudioPlayer;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        spy = spy(baseAudioPlayer);
        stateLoggingAudioPlayer = new StateLoggingAudioPlayer(stateRepo, spy, playerIdRepo, scheduler);
    }

    @Test
    public void test_DidDecorateStartPlay() {
        stateLoggingAudioPlayer.playerStart("test");

        verify(spy).playerStart("test");
    }

    @Test
    public void test_DidDecorateStopPlay() {
        stateLoggingAudioPlayer.playerStop();

        verify(spy).playerStop();
    }

    @Test
    public void test_OnStartPlay_PostState() {
        stateLoggingAudioPlayer.playerStart("test");

        //Then
        stateRepo.observe()
                .test()
                .assertValue(state -> state == MainView.State.Playing);
    }

    @Test
    public void test_OnStopPlayPostState(){
        stateLoggingAudioPlayer.playerStop();

        //Then
        stateRepo.observe()
                .test()
                .assertValue(state -> state == MainView.State.Idle);
    }

    @Test
    public void test_PlayerId_PostToRepo() {
        audioEvents.onNext(new AudioPlayer.SendPlayerId(10));

        playerIdRepo.observe().test()
                .assertValue(integerOneShot ->
                        integerOneShot.getValueOnce() == 10
                );
    }

    @Test
    public void test_OnPlayerError_PostIdleState() {
        audioEvents.onNext(new AudioPlayer.Error("test"));

        stateRepo.observe().test()
                .assertValue(state ->
                        state == MainView.State.Idle
                );
    }

    @Test
    public void test_DidDecorateIsPlaying(){
        assert stateLoggingAudioPlayer.isPlaying();
    }
}