package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.files.FilePathGenerator
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.app.recording.recorder.RecorderDefaults
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
