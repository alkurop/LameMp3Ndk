package com.omar.retromp3recorder.di

import com.nhaarman.mockitokotlin2.mock
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.share.Sharer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class TestFunctionalityModule {

    @Provides
    @Singleton
    fun provideAudioPlayer(): AudioPlayer = mock()

    @Provides
    @Singleton
    fun provideVoiceRecorder(): Mp3VoiceRecorder = mock()

    @Provides
    @Singleton
    fun provideSharingModuleInternal(): Sharer = mock()

}
