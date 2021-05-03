package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.state.repos.CanRenameFileRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestRepoModule {
    @Singleton
    @Provides
    fun provideCanRenameRepo(): CanRenameFileRepo {
        return CanRenameFileRepo()
    }
}