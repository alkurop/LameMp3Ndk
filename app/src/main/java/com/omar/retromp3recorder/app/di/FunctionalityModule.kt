package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.playback.player.*
import com.omar.retromp3recorder.app.recording.recorder.*
import com.omar.retromp3recorder.app.share.LoggingSharingModule
import com.omar.retromp3recorder.app.share.SharingModule
import com.omar.retromp3recorder.app.share.SharingModuleRX
import dagger.Binds
import dagger.Module
import javax.inject.Named
import javax.inject.Singleton

@Module
internal interface FunctionalityModule {
    @Singleton
    @Named(AppComponent.DECORATOR_A)
    @Binds
    fun provideAudioPlayerBase(clazz: AudioPlayerRx): AudioPlayer

    @Singleton
    @Named(AppComponent.DECORATOR_B)
    @Binds
    fun provideAudioStateLoggingPlayer(clazz: LoggingAudioPlayer): AudioPlayer

    @Singleton
    @Binds
    fun provideAudioLoggingPlayer(clazz: StateLoggingAudioPlayer): AudioPlayer

    @Singleton
    @Named(AppComponent.DECORATOR_A)
    @Binds
    fun provideVoiceRecorderBase(clazz: VoiceRecorderRX): VoiceRecorder

    @Singleton
    @Named(AppComponent.DECORATOR_B)
    @Binds
    fun provideVoiceLoggingRecorder(clazz: LoggingVoiceRecorder): VoiceRecorder

    @Singleton
    @Binds
    fun provideVoiceStateLoggingRecorder(clazz: StateLoggingVoiceRecorder): VoiceRecorder

    @Singleton
    @Named(AppComponent.DECORATOR_A)
    @Binds
    fun provideSharingModuleBase(clazz: SharingModuleRX): SharingModule

    @Singleton
    @Binds
    fun provideSharingLoggingModule(clazz: LoggingSharingModule): SharingModule

    @Binds
    fun provideStatefulAudioRecorder(clazz: VoiceRecorderRX): StatefulVoiceRecorder

    @Binds
    fun provideStatefulAudioPlayer(clazz: AudioPlayerRx): StatefulAudioPlayer
}
