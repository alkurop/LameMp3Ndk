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
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static org.mockito.Mockito.mock;

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
        return mock(ChangeBitrateUC.class);
    }

    @Singleton
    @Provides
    ChangeSampleRateUC provideChangeSampleRateUC() {
        return mock(ChangeSampleRateUC.class);
    }

    @Singleton
    @Provides
    CheckPermissionsUC provideCheckPermissionsUC() {
        return mock(CheckPermissionsUC.class);
    }

    @Singleton
    @Provides
    ShareUC provideShareUC() {
        return mock(ShareUC.class);
    }

    @Singleton
    @Provides
    StartRecordUC provideStartRecordUC() {
        return mock(StartRecordUC.class);
    }

    @Singleton
    @Provides
    StartPlaybackUC provideStartPlaybackUC() {
        return mock(StartPlaybackUC.class);
    }

    @Singleton
    @Provides
    StopPlaybackAndRecordUC provideStopPlaybackAndRecordUC() {
        return mock(StopPlaybackAndRecordUC.class);
    }
}