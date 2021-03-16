package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.modules.playback.AudioPlayer
import com.omar.retromp3recorder.app.modules.playback.AudioPlayerImpl
import com.omar.retromp3recorder.app.modules.share.Sharer
import com.omar.retromp3recorder.app.modules.share.SharerImpl
import com.omar.retromp3recorder.app.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.app.recorder.Mp3VoiceRecorderImpl
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
