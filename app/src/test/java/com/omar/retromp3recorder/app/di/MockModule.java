package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.player.AudioPlayer;
import com.omar.retromp3recorder.app.recorder.VoiceRecorder;
import com.omar.retromp3recorder.app.share.SharingModule;
import com.omar.retromp3recorder.app.usecase.ChangeBitrateUC;
import com.omar.retromp3recorder.app.usecase.ChangeBitrateUCImpl;
import com.omar.retromp3recorder.app.usecase.ChangeSampleRateUC;
import com.omar.retromp3recorder.app.usecase.ChangeSampleRateUCImpl;
import com.omar.retromp3recorder.app.usecase.CheckPermissionsUC;
import com.omar.retromp3recorder.app.usecase.CheckPermissionsUCImpl;
import com.omar.retromp3recorder.app.usecase.ShareUC;
import com.omar.retromp3recorder.app.usecase.ShareUCImpl;
import com.omar.retromp3recorder.app.usecase.StartPlaybackUC;
import com.omar.retromp3recorder.app.usecase.StartPlaybackUCImpl;
import com.omar.retromp3recorder.app.usecase.StartRecordUC;
import com.omar.retromp3recorder.app.usecase.StartRecordUCImpl;
import com.omar.retromp3recorder.app.usecase.StopPlaybackAndRecordUC;
import com.omar.retromp3recorder.app.usecase.StopPlaybackAndRecordUCImpl;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Binds;
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
    ChangeBitrateUC provideChangeBitrateUC() {
        ChangeBitrateUC mock = mock(ChangeBitrateUC.class);
        when(mock.execute(any())).thenReturn(Completable.complete());
        return mock;
    }

    @Singleton
    @Provides
    ChangeSampleRateUC provideChangeSampleRateUC() {
        ChangeSampleRateUC mock = mock(ChangeSampleRateUC.class);
        when(mock.execute(any())).thenReturn(Completable.complete());
        return mock;
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

    @Singleton
    @Provides
    StartRecordUC provideStartRecordUC() {
        StartRecordUC mock = mock(StartRecordUC.class);
        when(mock.execute()).thenReturn(Completable.complete());
        return mock;
    }

    @Singleton
    @Provides
    StartPlaybackUC provideStartPlaybackUC() {
        StartPlaybackUC mock = mock(StartPlaybackUC.class);
        when(mock.execute()).thenReturn(Completable.complete());
        return mock;
    }

    @Singleton
    @Provides
    StopPlaybackAndRecordUC provideStopPlaybackAndRecordUC() {
        StopPlaybackAndRecordUC mock = mock(StopPlaybackAndRecordUC.class);
        when(mock.execute()).thenReturn(Completable.complete());
        return mock;
    }
}