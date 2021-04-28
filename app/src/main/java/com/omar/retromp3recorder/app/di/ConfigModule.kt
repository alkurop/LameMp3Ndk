package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.iorecorder.RecorderDefaults
import dagger.Module
import dagger.Provides

@Module
class ConfigModule {
    @Provides
    fun provideRecorderDefaults(): RecorderDefaults {
        return RecorderDefaults(
            Mp3VoiceRecorder.BitRate._320,
            Mp3VoiceRecorder.SampleRate._44100
        )
    }

}
