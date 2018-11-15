package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.main.MainViewInteractorActionsTest;
import com.omar.retromp3recorder.app.main.MainViewInteractorRepoTest;
import com.omar.retromp3recorder.app.playback.player.LoggingAudioPlayerTest;
import com.omar.retromp3recorder.app.playback.player.StateLoggingAudioPlayerTest;
import com.omar.retromp3recorder.app.recording.recorder.LoggingVoiceRecorderTest;
import com.omar.retromp3recorder.app.recording.recorder.StateLoggingVoiceRecorderTest;
import com.omar.retromp3recorder.app.share.LoggingSharingModuleTest;
import com.omar.retromp3recorder.app.shared.usecase.ChangeBitrateUCTest;
import com.omar.retromp3recorder.app.shared.usecase.ChangeSampleRateUCImplTest;
import com.omar.retromp3recorder.app.shared.usecase.StartPlaybackUCImplTest;
import com.omar.retromp3recorder.app.shared.usecase.StartRecordUCImplTest;
import com.omar.retromp3recorder.app.shared.usecase.StopPlaybackAndRecordUCTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ConfigModule.class,
        TestFunctionalityModule.class,
        TestUtilsModule.class,
        MockModule.class
})
public interface TestAppComponent {

    void inject(MainViewInteractorRepoTest mainViewInteractorRepoTest);

    void inject(MainViewInteractorActionsTest mainViewInteractorActionsTest);

    void inject(LoggingAudioPlayerTest loggingAudioPlayerTest);

    void inject(StateLoggingAudioPlayerTest stateLoggingAudioPlayerTest);

    void inject(LoggingVoiceRecorderTest loggingVoiceRecorderTest);

    void inject(StateLoggingVoiceRecorderTest stateLoggingVoiceRecorderTest);

    void inject(ChangeBitrateUCTest changeBitrateUCImplTest);

    void inject(ChangeSampleRateUCImplTest changeSampleRateUCImplTest);

    void inject(LoggingSharingModuleTest loggingSharingModuleTest);

    void inject(StartPlaybackUCImplTest startPlaybackUCImplTest);

    void inject(StartRecordUCImplTest startRecordUCImplTest);

    void inject(StopPlaybackAndRecordUCTest stopPlaybackAndRecordUCImplTest);
}