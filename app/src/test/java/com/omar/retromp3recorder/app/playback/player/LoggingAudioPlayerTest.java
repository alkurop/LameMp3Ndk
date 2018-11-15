package com.omar.retromp3recorder.app.playback.player;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.shared.repo.LogRepo;

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

public class LoggingAudioPlayerTest {

    @Inject
    LogRepo logRepo;
    @Inject
    Scheduler scheduler;
    @Inject
    @Named(DECORATOR_ALPHA)
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
        audioEvents.onNext(new AudioPlayer.Message("test"));

        //Then
        logRepo.observe()
                .test()
                .assertValue(event ->
                        "test".equals(((LogRepo.Message) event).message)
                );
    }

    @Test
    public void test_OnPlayerError_PostLog() {
        audioEvents.onNext(new AudioPlayer.Error("test"));

        //Then
        logRepo.observe()
                .test()
                .assertValue(event ->
                        "test".equals(((LogRepo.Error) event).error)
                );
    }

    @Test
    public void test_DidDecorateIsPlaying(){
        assert loggingAudioPlayer.isPlaying();
    }

}