package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.playback.player.AudioPlayer
import com.omar.retromp3recorder.app.playback.player.TestAudioPlayer
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.app.recording.recorder.TestVoiceRecorder
import com.omar.retromp3recorder.app.share.Sharer
import com.omar.retromp3recorder.app.share.TestSharingModule
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
