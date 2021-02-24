package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.files.FilePathGenerator
import com.omar.retromp3recorder.app.recording.recorder.RecorderDefaults
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class ConfigModule {
    @Singleton
    @Provides
    fun provideRecorderDefaults(filePathGenerator: FilePathGenerator): RecorderDefaults {
        return RecorderDefaults(
            VoiceRecorder.BitRate._320,
            VoiceRecorder.SampleRate._44100,
            filePathGenerator.generateFilePath()
        )
    }
}
