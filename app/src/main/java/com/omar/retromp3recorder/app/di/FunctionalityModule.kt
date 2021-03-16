package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.audioplayer.AudioPlayerImpl
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorderImpl
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.share.SharerImpl
import dagger.Binds
import dagger.Module

@Module
internal interface FunctionalityModule {
    @Binds
    fun provideAudioPlayerBase(clazz: AudioPlayerImpl): AudioPlayer

    @Binds
    fun provideVoiceRecorderBase(clazz: Mp3VoiceRecorderImpl): Mp3VoiceRecorder

    @Binds
    fun provideSharingModuleBase(clazz: SharerImpl): Sharer

}
