package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.recording.usecase.CheckPermissionsUC
import com.omar.retromp3recorder.app.recording.usecase.CheckPermissionsUCImpl
import dagger.Binds
import dagger.Module

@Module
interface UseCaseModule {
    @Binds
    fun provideCheckPermissionsUC(impl: CheckPermissionsUCImpl): CheckPermissionsUC
}