package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.state.repos.AudioStateMapper
import com.omar.retromp3recorder.state.repos.AudioStateMapperImpl
import dagger.Binds
import dagger.Module

@Module
interface RepoModule {
    @Binds
    fun provideAudioStateRepo(repo: AudioStateMapperImpl): AudioStateMapper
}