package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.player.AudioPlayerRx;
import com.omar.retromp3recorder.app.player.LoggingAudioPlayer;
import com.omar.retromp3recorder.app.player.StateLoggingAudioPlayer;
import com.omar.retromp3recorder.app.recorder.LoggingVoiceRecorder;
import com.omar.retromp3recorder.app.recorder.StateLoggingVoiceRecorder;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.recorder.VoiceRecorderRX;
import com.omar.retromp3recorder.app.share.LoggingSharingModule;
import com.omar.retromp3recorder.app.share.SharingModule;
import com.omar.retromp3recorder.app.share.SharingModuleRX;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_ALPHA;
import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_BETA;

@Module(
        subcomponents = {ConfigSubComponent.class}
)
public abstract class FunctionalityModule {

    //region audio player
    @Singleton
    @Named(DECORATOR_ALPHA)
    @Binds
    abstract AudioPlayer provideAudioPlayerInternal(AudioPlayerRx clazz);

    @Singleton
    @Named(DECORATOR_BETA)
    @Binds
    abstract AudioPlayer provideAudioStateLoggingPlayer(LoggingAudioPlayer clazz);

    @Singleton
    @Binds
    abstract AudioPlayer provideAudioLoggingPlayer(StateLoggingAudioPlayer clazz);

    //endregion

    //region voice recorder

    @Singleton
    @Named(DECORATOR_ALPHA)
    @Binds
    abstract VoiceRecorder provideVoiceRecorderInternal(VoiceRecorderRX clazz);

    @Singleton
    @Named(DECORATOR_BETA)
    @Binds
    abstract VoiceRecorder provideVoiceLoggingRecorder(LoggingVoiceRecorder clazz);

    @Singleton
    @Binds
    abstract VoiceRecorder provideVoiceStateLoggingRecorder(StateLoggingVoiceRecorder clazz);
    //endregion

    //region share

    @Singleton
    @Named(DECORATOR_ALPHA)
    @Binds
    abstract SharingModule provideSharingModuleInternal(SharingModuleRX clazz);

    @Singleton
    @Binds
    abstract SharingModule provideSharingLoggingModule(LoggingSharingModule clazz);

    //endregion
}
