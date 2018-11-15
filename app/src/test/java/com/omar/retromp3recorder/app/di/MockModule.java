package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.playback.player.AudioPlayer;
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.share.SharingModule;
import com.omar.retromp3recorder.app.recording.usecase.CheckPermissionsUC;
import com.omar.retromp3recorder.app.share.ShareUC;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Completable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Singleton
    @Provides
    CheckPermissionsUC provideCheckPermissionsUC() {
        CheckPermissionsUC mock = mock(CheckPermissionsUC.class);
        when(mock.execute(any())).thenReturn(Completable.complete());
        return mock;
    }

    @Singleton
    @Provides
    ShareUC provideShareUC() {
        ShareUC mock = mock(ShareUC.class);
        when(mock.execute()).thenReturn(Completable.complete());
        return mock;
    }
}