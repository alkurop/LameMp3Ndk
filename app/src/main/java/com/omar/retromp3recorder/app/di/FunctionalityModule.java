package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.player.AudioPlayerRx;
import com.omar.retromp3recorder.app.player.LoggingAudioPlayer;
import com.omar.retromp3recorder.app.recorder.LoggingVoiceRecorder;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.recorder.VoiceRecorderRX;
import com.omar.retromp3recorder.app.share.LogginSharingModule;
import com.omar.retromp3recorder.app.share.SharingModule;
import com.omar.retromp3recorder.app.share.SharingModuleRX;

import javax.inject.Named;

import dagger.Binds;
import dagger.Module;

import static com.omar.retromp3recorder.app.di.AppComponent.INTERNAL;

@Module(
        subcomponents = {RepoSubComponent.class}
)
abstract class FunctionalityModule {

    @Binds
    abstract VoiceRecorder provideVoiceRecorder(LoggingVoiceRecorder recorder);

    @Binds
    abstract AudioPlayer provideAudioPlayer(LoggingAudioPlayer player);

    @Named(INTERNAL)
    @Binds
    abstract AudioPlayer provideAudioPlayerInternal(AudioPlayerRx player);

    @Named(INTERNAL)
    @Binds
    abstract VoiceRecorder provideVoiceRecorderInternal(VoiceRecorderRX recorder);


    @Named(INTERNAL)
    @Binds
    abstract SharingModule provideSharingModuleInternal(SharingModuleRX recorder);

    @Binds
    abstract SharingModule provideSharingModule(LogginSharingModule recorder);

}
