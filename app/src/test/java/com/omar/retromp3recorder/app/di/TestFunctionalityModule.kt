package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.modules.TestAudioPlayer
import com.omar.retromp3recorder.app.modules.TestSharingModule
import com.omar.retromp3recorder.app.modules.TestVoiceRecorder
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.share.Sharer
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
interface TestFunctionalityModule {

    @Singleton
    @Binds
    fun provideAudioPlayer (clazz: TestAudioPlayer): AudioPlayer

    @Singleton
    @Binds
    fun provideVoiceRecorder (clazz: TestVoiceRecorder): Mp3VoiceRecorder

    @Singleton
    @Binds
    fun provideSharingModuleInternal(clazz: TestSharingModule): Sharer

}
