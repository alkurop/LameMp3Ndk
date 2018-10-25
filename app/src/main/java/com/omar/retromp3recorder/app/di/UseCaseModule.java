package com.omar.retromp3recorder.app.di;

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

import dagger.Binds;
import dagger.Module;

@Module
public interface UseCaseModule {

    @Binds
    ChangeBitrateUC provideChangeBitrateUC(ChangeBitrateUCImpl impl);

    @Binds
    ChangeSampleRateUC provideChangeSampleRateUC(ChangeSampleRateUCImpl impl);

    @Binds
    CheckPermissionsUC provideCheckPermissionsUC(CheckPermissionsUCImpl impl);

    @Binds
    ShareUC provideShareUC(ShareUCImpl impl);

    @Binds
    StartRecordUC provideStartRecordUC(StartRecordUCImpl impl);

    @Binds
    StartPlaybackUC provideStartPlaybackUC(StartPlaybackUCImpl impl);

    @Binds
    StopPlaybackAndRecordUC provideStopPlaybackAndRecordUC(StopPlaybackAndRecordUCImpl impl);
}
