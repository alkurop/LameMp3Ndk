package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.player.AudioPlayerRx;
import com.omar.retromp3recorder.app.player.LoggingAudioPlayer;
import com.omar.retromp3recorder.app.recorder.LoggingVoiceRecorder;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.recorder.VoiceRecorderRX;
import com.omar.retromp3recorder.app.share.LoggingSharingModule;
import com.omar.retromp3recorder.app.share.SharingModule;
import com.omar.retromp3recorder.app.share.SharingModuleRX;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

import static com.omar.retromp3recorder.app.di.AppComponent.INTERNAL;

@Module(
        subcomponents = {ConfigSubComponent.class}
)
public abstract class FunctionalityModule {

    @Singleton
    @Binds
    abstract VoiceRecorder provideVoiceRecorder(LoggingVoiceRecorder clazz);

    @Singleton
    @Binds
    abstract AudioPlayer provideAudioPlayer(LoggingAudioPlayer clazz);

    @Singleton
    @Named(INTERNAL)
    @Binds
    abstract AudioPlayer provideAudioPlayerInternal(AudioPlayerRx clazz);

    @Singleton
    @Named(INTERNAL)
    @Binds
    abstract VoiceRecorder provideVoiceRecorderInternal(VoiceRecorderRX clazz);

    @Singleton
    @Named(INTERNAL)
    @Binds
    abstract SharingModule provideSharingModuleInternal(SharingModuleRX clazz);

    @Singleton
    @Binds
    abstract SharingModule provideSharingModule(LoggingSharingModule clazz);

}
