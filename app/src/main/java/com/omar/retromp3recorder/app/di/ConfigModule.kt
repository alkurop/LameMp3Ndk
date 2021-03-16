package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.modules.files.FilePathGenerator
import com.omar.retromp3recorder.app.modules.recording.Mp3VoiceRecorder
import com.omar.retromp3recorder.app.modules.recording.RecorderDefaults
import dagger.Module
import dagger.Provides

@Module
internal class ConfigModule {
    @Provides
    fun provideRecorderDefaults(filePathGenerator: FilePathGenerator): RecorderDefaults {
        return RecorderDefaults(
            Mp3VoiceRecorder.BitRate._320,
            Mp3VoiceRecorder.SampleRate._44100,
            filePathGenerator.generateFilePath()
        )
    }
}
