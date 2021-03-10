package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.playback.player.AudioPlayer
import com.omar.retromp3recorder.app.playback.player.AudioPlayerIO
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorderIO
import com.omar.retromp3recorder.app.share.Sharer
import com.omar.retromp3recorder.app.share.SharerIO
import dagger.Binds
import dagger.Module

@Module
internal interface FunctionalityModule {
    @Binds
    fun provideAudioPlayerBase(clazz: AudioPlayerIO): AudioPlayer

    @Binds
    fun provideVoiceRecorderBase(clazz: VoiceRecorderIO): VoiceRecorder

    @Binds
    fun provideSharingModuleBase(clazz: SharerIO): Sharer

}
