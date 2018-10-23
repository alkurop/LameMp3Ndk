package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.recorder.FilePathGenerator;
import com.omar.retromp3recorder.app.recorder.RecorderDefaults;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
class ConfigModule {
    @Singleton
    @Provides
    RecorderDefaults provideRecorderDefaults(FilePathGenerator filePathGenerator) {
        return new RecorderDefaults(VoiceRecorder.BitRate._320, VoiceRecorder.SampleRate._44100, filePathGenerator.generateFilePath());
    }
}
