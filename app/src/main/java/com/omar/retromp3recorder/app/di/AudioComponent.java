package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.player.AudioPlayer;

import dagger.Subcomponent;

@Subcomponent(modules = AudioModule.class)
public interface AudioComponent {

    VoiceRecorder getAudioRecorder();

    AudioPlayer getAudioPlayer();
}
