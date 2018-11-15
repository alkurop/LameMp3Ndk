package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.player.LoggingAudioPlayer;
import com.omar.retromp3recorder.app.player.StateLoggingAudioPlayer;
import com.omar.retromp3recorder.app.player.TestAudioPlayer;
import com.omar.retromp3recorder.app.recorder.LoggingVoiceRecorder;
import com.omar.retromp3recorder.app.recorder.StateLoggingVoiceRecorder;
import com.omar.retromp3recorder.app.recorder.TestVoiceRecorder;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.share.LoggingSharingModule;
import com.omar.retromp3recorder.app.share.SharingModule;
import com.omar.retromp3recorder.app.share.TestSharingModule;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_ALPHA;
import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_BETA;


@Module(
        subcomponents = {ConfigSubComponent.class}
)
abstract class TestFunctionalityModule {

    @Singleton
    @Named(DECORATOR_BETA)
    @Binds
    abstract VoiceRecorder provideLoggingVoiceRecorder(LoggingVoiceRecorder clazz);

    @Singleton
    @Named(DECORATOR_BETA)
    @Binds
    abstract AudioPlayer provideLoggingAudioPlayer(LoggingAudioPlayer clazz);

    @Singleton
    @Binds
    abstract VoiceRecorder provideStateVoiceRecorder(StateLoggingVoiceRecorder clazz);

    @Singleton
    @Binds
    abstract AudioPlayer provideStateAudioPlayer(StateLoggingAudioPlayer clazz);

    @Singleton
    @Named(DECORATOR_ALPHA)
    @Binds
    abstract AudioPlayer provideAudioPlayerInternal(TestAudioPlayer clazz);

    @Singleton
    @Named(DECORATOR_ALPHA)
    @Binds
    abstract VoiceRecorder provideVoiceRecorderInternal(TestVoiceRecorder clazz);

    @Singleton
    @Named(DECORATOR_ALPHA)
    @Binds
    abstract SharingModule provideSharingModuleInternal(TestSharingModule clazz);

    @Singleton
    @Binds
    abstract SharingModule provideSharingModule(LoggingSharingModule clazz);

}
