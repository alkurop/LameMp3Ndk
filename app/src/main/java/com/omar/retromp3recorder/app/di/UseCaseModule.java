package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.usecase.CheckPermissionsUC;
import com.omar.retromp3recorder.app.usecase.CheckPermissionsUCImpl;

import dagger.Binds;
import dagger.Module;

@Module
public interface UseCaseModule {

    @Binds
    CheckPermissionsUC provideCheckPermissionsUC(CheckPermissionsUCImpl impl);

}
