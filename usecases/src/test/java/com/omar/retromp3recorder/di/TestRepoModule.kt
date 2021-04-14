package com.omar.retromp3recorder.di

import com.nhaarman.mockitokotlin2.mock
import com.omar.retromp3recorder.state.repos.AudioStateRepo
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestRepoModule {

    @Singleton
    @Provides
    fun provideAudioStateRepo(): AudioStateRepo {
        return mock()
    }
}