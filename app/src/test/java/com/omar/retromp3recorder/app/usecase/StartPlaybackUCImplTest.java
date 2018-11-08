package com.omar.retromp3recorder.app.usecase;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.repo.FileNameRepo;
import com.omar.retromp3recorder.app.repo.RequestPermissionsRepo;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_ALPHA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StartPlaybackUCImplTest {

    @Inject
    FileNameRepo fileNameRepo;
    @Inject
    RequestPermissionsRepo requestPermissionsRepo;

    @Inject
    @Named(DECORATOR_ALPHA)
    AudioPlayer audioPlayer;
    @Inject
    VoiceRecorder voiceRecorder;

    @Inject
    CheckPermissionsUC checkPermissionsUC;
    @Inject
    StopPlaybackAndRecordUC stopPlaybackAndRecordUC;

    private StartPlaybackUCImpl startPlaybackUC;
    private AudioPlayer spyAudioPlayer;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        spyAudioPlayer = spy(audioPlayer);
        when(checkPermissionsUC.execute(any()))
                .thenAnswer(invocation -> {
                    requestPermissionsRepo.newValue(new RequestPermissionsRepo.Granted());
                    return Completable.complete();
                });
        startPlaybackUC = new StartPlaybackUCImpl(
                fileNameRepo,
                spyAudioPlayer,
                voiceRecorder,
                stopPlaybackAndRecordUC,
                checkPermissionsUC,
                requestPermissionsRepo
        );
    }

    @Test
    public void test_UC_StartedAudioPlayback() {
        startPlaybackUC.execute().subscribe();

        verify(spyAudioPlayer).playerStart(any());
    }
}
