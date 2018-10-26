package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.main.MainViewInteractorActionsTest;
import com.omar.retromp3recorder.app.main.MainViewInteractorRepoTest;
import com.omar.retromp3recorder.app.player.LoggingAudioPlayerTest;
import com.omar.retromp3recorder.app.player.StateLoggingAudioPlayerTest;
import com.omar.retromp3recorder.app.recorder.LoggingVoiceRecorderTest;
import com.omar.retromp3recorder.app.recorder.StateLoggingVoiceRecorderTest;

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

}
