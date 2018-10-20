package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.player.AudioPlayerRx;
import com.omar.retromp3recorder.app.recorder.VoiceRecorderRX;

import dagger.Module;
import dagger.Provides;

@Module( )
public class AudioModule {


    @Provides
    AudioPlayer provideAudioPlayer(StringProvider stringProvider) {
        return new AudioPlayerRx(stringProvider);
    }

    @Provides
    VoiceRecorder providesAudioRecorder(StringProvider stringProvider) {
        return new VoiceRecorderRX(stringProvider);
    }
}
