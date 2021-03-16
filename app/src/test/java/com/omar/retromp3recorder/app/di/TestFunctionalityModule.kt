package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.modules.audioplayer.AudioPlayer
import com.omar.retromp3recorder.app.modules.audioplayer.TestAudioPlayer
import com.omar.retromp3recorder.app.modules.recording.TestVoiceRecorder
import com.omar.retromp3recorder.app.modules.share.Sharer
import com.omar.retromp3recorder.app.modules.sharing.TestSharingModule
import com.omar.retromp3recorder.app.recorder.Mp3VoiceRecorder
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
