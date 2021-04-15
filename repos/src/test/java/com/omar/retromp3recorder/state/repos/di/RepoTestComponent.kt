package com.omar.retromp3recorder.state.repos.di

import com.omar.retromp3recorder.state.repos.AudioStateRepoImplTest
import dagger.Component
import javax.inject.Singleton

@Component
@Singleton
internal interface RepoTestComponent {
    fun inject(audioStateRepoImplTest: AudioStateRepoImplTest)
}