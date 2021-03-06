package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.playback.player.AudioPlayer
import com.omar.retromp3recorder.app.playback.player.LoggingAudioPlayer
import com.omar.retromp3recorder.app.playback.player.StateLoggingAudioPlayer
import com.omar.retromp3recorder.app.playback.player.TestAudioPlayer
import com.omar.retromp3recorder.app.recording.recorder.LoggingVoiceRecorder
import com.omar.retromp3recorder.app.recording.recorder.StateLoggingVoiceRecorder
import com.omar.retromp3recorder.app.recording.recorder.TestVoiceRecorder
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder
import com.omar.retromp3recorder.app.share.LoggingSharingModule
import com.omar.retromp3recorder.app.share.SharingModule
import com.omar.retromp3recorder.app.share.TestSharingModule
import dagger.Binds
import dagger.Module
import javax.inject.Named
import javax.inject.Singleton

@Module
interface TestFunctionalityModule {
    @Singleton
    @Named(AppComponent.DECORATOR_B)
    @Binds
    fun provideLoggingVoiceRecorder(clazz: LoggingVoiceRecorder): VoiceRecorder

    @Singleton
    @Named(AppComponent.DECORATOR_B)
    @Binds
    fun provideLoggingAudioPlayer(clazz: LoggingAudioPlayer): AudioPlayer

    @Singleton
    @Binds
    fun provideStateVoiceRecorder(clazz: StateLoggingVoiceRecorder): VoiceRecorder

    @Singleton
    @Binds
    fun provideStateAudioPlayer(clazz: StateLoggingAudioPlayer): AudioPlayer

    @Singleton
    @Named(AppComponent.DECORATOR_A)
    @Binds
    fun provideAudioPlayerInternal(clazz: TestAudioPlayer): AudioPlayer

    @Singleton
    @Named(AppComponent.DECORATOR_A)
    @Binds
    fun provideVoiceRecorderInternal(clazz: TestVoiceRecorder): VoiceRecorder

    @Singleton
    @Named(AppComponent.DECORATOR_A)
    @Binds
    fun provideSharingModuleInternal(clazz: TestSharingModule): SharingModule

    @Singleton
    @Binds
    fun provideSharingModule(clazz: LoggingSharingModule): SharingModule
}
