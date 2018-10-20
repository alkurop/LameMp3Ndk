package com.omar.retromp3recorder.app.di;

import dagger.Subcomponent;

@Subcomponent(modules = AudioModule.class)
public interface AudioComponent {

    VoiceRecorder getAudioRecorder();

    AudioPlayer getAudioPlayer();
}
