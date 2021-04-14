package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.state.repos.AudioStateRepo
import com.omar.retromp3recorder.state.repos.AudioStateRepoImpl
import dagger.Binds
import dagger.Module

@Module
interface RepoModule {
    @Binds
    fun provideAudioStateRepo(repo: AudioStateRepoImpl): AudioStateRepo
}