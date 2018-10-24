package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.share.SharingModule;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

@Module
public class MockModule {

    public static final String RECORDER_SUBJECT = "RECORDER_SUBJECT";
    public static final String PLAYER_SUBJECT = "PLAYER_SUBJECT";
    public static final String SHARING_SUBJECT = "SHARING_SUBJECT";

    @Singleton
    @Provides
    @Named(RECORDER_SUBJECT)
    Subject<VoiceRecorder.Event> provideRecorderSubject() {
        return PublishSubject.create();
    }

    @Singleton
    @Provides
    @Named(PLAYER_SUBJECT)
    Subject<AudioPlayer.Event> providePlayerSubject() {
        return PublishSubject.create();
    }

    @Singleton
    @Provides
    @Named(SHARING_SUBJECT)
    Subject<SharingModule.Event> provideSharingSubject() {
        return PublishSubject.create();
    }
}