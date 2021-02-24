package com.omar.retromp3recorder.app.playback.player;

import com.omar.retromp3recorder.app.common.repo.LogRepo;
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.subjects.Subject;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_A;
import static com.omar.retromp3recorder.app.di.MockModule.PLAYER_SUBJECT;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class LoggingAudioPlayerTest {

    @Inject
    LogRepo logRepo;
    @Inject
    Scheduler scheduler;
    @Inject
    @Named(DECORATOR_A)
    AudioPlayer baseAudioPlayer;
    @Inject
    @Named(PLAYER_SUBJECT)
    Subject<AudioPlayer.Event> audioEvents;

    private AudioPlayer spyAudioPlayer;

    private LoggingAudioPlayer loggingAudioPlayer;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        spyAudioPlayer = spy(baseAudioPlayer);
        loggingAudioPlayer = new LoggingAudioPlayer(spyAudioPlayer, scheduler, logRepo);
    }

    @Test
    public void test_DidDecorateStartPlay() {
        loggingAudioPlayer.playerStart("test");

        //Then
        verify(spyAudioPlayer).playerStart("test");
    }

    @Test
    public void test_DidDecorateStopPlay() {
        loggingAudioPlayer.playerStop();

        //Then
        verify(spyAudioPlayer).playerStop();
    }

    @Test
    public void test_OnPlayerMessage_PostLog() {
        audioEvents.onNext(new AudioPlayer.Event.Message("test"));

        //Then
        logRepo.observe()
                .test()
                .assertValue(event ->
                        "test".equals(((LogRepo.Event.Message) event).getMessage())
                );
    }

    @Test
    public void test_OnPlayerError_PostLog() {
        audioEvents.onNext(new AudioPlayer.Event.Error("test"));

        //Then
        logRepo.observe()
                .test()
                .assertValue(event ->
                        "test".equals(((LogRepo.Event.Error) event).getError())
                );
    }

    @Test
    public void test_DidDecorateIsPlaying(){
        assert loggingAudioPlayer.isPlaying();
    }

}