package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.PlayButtonStateMapperTest
import com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate.ShareButtonStateMapperTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ConfigModule::class,
        MockModule::class,
        RepoModule::class,
        TestFunctionalityModule::class,
        TestUtilsModule::class,
        TestStorageModule::class,
    ]
)
interface TestAppComponent {
    fun inject(playButtonStateMapperTest: PlayButtonStateMapperTest)
    fun inject(shareButtonStateMapperTest: ShareButtonStateMapperTest)
}