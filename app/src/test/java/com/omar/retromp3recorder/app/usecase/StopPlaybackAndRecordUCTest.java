package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_ALPHA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class StopPlaybackAndRecordUCTest {
    @Inject
    @Named(DECORATOR_ALPHA)
    AudioPlayer baseAudioPlayer;

    @Inject
    @Named(DECORATOR_ALPHA)
    VoiceRecorder baseVoiceRecorder;

    private AudioPlayer audioPlayer;
    private VoiceRecorder voiceRecorder;
    private StopPlaybackAndRecordUC stopPlaybackAndRecordUC;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        audioPlayer = spy(baseAudioPlayer);
        voiceRecorder = spy(baseVoiceRecorder);
        stopPlaybackAndRecordUC = new StopPlaybackAndRecordUC(audioPlayer, voiceRecorder);
    }

    @Test
    public void test_StopRecordsAndPlayback() {
        stopPlaybackAndRecordUC.execute().subscribe();

        //Then
        verify(audioPlayer).playerStop();
        verify(voiceRecorder).stopRecord();
    }
}