package com.omar.retromp3recorder.app.shared.usecase;

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;
import com.omar.retromp3recorder.app.recording.recorder.FilePathGenerator;
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.recording.repo.BitRateRepo;
import com.omar.retromp3recorder.app.recording.usecase.CheckPermissionsUC;
import com.omar.retromp3recorder.app.recording.usecase.StartRecordUC;
import com.omar.retromp3recorder.app.shared.repo.FileNameRepo;
import com.omar.retromp3recorder.app.shared.repo.RequestPermissionsRepo;
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_ALPHA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StartRecordUCImplTest {
    @Inject
    FileNameRepo fileNameRepo;
    @Inject
    BitRateRepo bitRateRepo;
    @Inject
    SampleRateRepo sampleRateRepo;
    @Inject
    @Named(DECORATOR_ALPHA)
    VoiceRecorder voiceRecorder;
    @Inject
    StopPlaybackAndRecordUC stopPlaybackAndRecordUC;
    @Inject
    CheckPermissionsUC checkPermissionsUC;
    @Inject
    RequestPermissionsRepo requestPermissionsRepo;
    @Mock
    FilePathGenerator filePathGenerator;

    private VoiceRecorder spyVoiceRecorder;
    private StartRecordUC startRecordUC;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        DaggerTestAppComponent.create().inject(this);
        spyVoiceRecorder = spy(voiceRecorder);
        when(checkPermissionsUC.execute(any()))
                .thenAnswer(invocation -> {
                    requestPermissionsRepo.newValue(new RequestPermissionsRepo.Granted());
                    return Completable.complete();
                });
        when(filePathGenerator.generateFilePath()).thenReturn("test");
        startRecordUC = new StartRecordUC(
                fileNameRepo,
                bitRateRepo,
                sampleRateRepo,
                spyVoiceRecorder,
                stopPlaybackAndRecordUC,
                checkPermissionsUC,
                requestPermissionsRepo,
                filePathGenerator
        );
    }

    @Test
    public void test_UC_Starts_Recording() {
        startRecordUC.execute().subscribe();

        //Then
        verify(spyVoiceRecorder).record(any());
    }

}