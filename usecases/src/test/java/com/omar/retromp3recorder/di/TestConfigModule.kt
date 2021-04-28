package com.omar.retromp3recorder.di

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.iorecorder.RecorderDefaults
import dagger.Module
import dagger.Provides

@Module
internal class TestConfigModule {
    @Provides
    fun provideRecorderDefaults(): RecorderDefaults {
        return RecorderDefaults(
            Mp3VoiceRecorder.BitRate._320,
            Mp3VoiceRecorder.SampleRate._44100
        )
    }
}