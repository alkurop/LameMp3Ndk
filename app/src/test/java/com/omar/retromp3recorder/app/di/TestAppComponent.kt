package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.common.usecase.ChangeBitrateUCTest
import com.omar.retromp3recorder.app.common.usecase.ChangeSampleRateUCImplTest
import com.omar.retromp3recorder.app.main.MainViewInteractorActionsTest
import com.omar.retromp3recorder.app.main.MainViewInteractorRepoTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ConfigModule::class, TestFunctionalityModule::class, TestUtilsModule::class, MockModule::class])
interface TestAppComponent {
    fun inject(mainViewInteractorRepoTest: MainViewInteractorRepoTest)
    fun inject(mainViewInteractorActionsTest: MainViewInteractorActionsTest)
    fun inject(changeBitrateUCImplTest: ChangeBitrateUCTest)
    fun inject(changeSampleRateUCImplTest: ChangeSampleRateUCImplTest)
}