package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.playback.player.AudioPlayer
import com.omar.retromp3recorder.app.playback.player.AudioPlayerRx
import com.omar.retromp3recorder.app.playback.player.StatefulAudioPlayer
import com.omar.retromp3recorder.app.recording.recorder.StatefulVoiceRecorder
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorderIO
import com.omar.retromp3recorder.app.share.Sharer
import com.omar.retromp3recorder.app.share.SharerIO
import dagger.Binds
import dagger.Module

@Module
internal interface FunctionalityModule {
    @Binds
    fun provideAudioPlayerBase(clazz: AudioPlayerRx): AudioPlayer

    @Binds
    fun provideVoiceRecorderBase(clazz: VoiceRecorderIO): VoiceRecorder

    @Binds
    fun provideSharingModuleBase(clazz: SharerIO): Sharer

    @Binds
    fun provideStatefulAudioRecorder(clazz: VoiceRecorderIO): StatefulVoiceRecorder

    @Binds
    fun provideStatefulAudioPlayer(clazz: AudioPlayerRx): StatefulAudioPlayer
}
