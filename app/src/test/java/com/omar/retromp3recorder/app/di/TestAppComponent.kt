package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.ui.main.MainViewInteractorActionsTest
import com.omar.retromp3recorder.app.ui.main.MainViewInteractorRepoTest
import com.omar.retromp3recorder.app.usecases.ChangeBitrateUCTest
import com.omar.retromp3recorder.app.usecases.ChangeSampleRateUCImplTest
import com.omar.retromp3recorder.app.usecases.ShareUCImplTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ConfigModule::class, TestFunctionalityModule::class, TestUtilsModule::class, MockModule::class])
interface TestAppComponent {
    fun inject(mainViewInteractorRepoTest: MainViewInteractorRepoTest)
    fun inject(mainViewInteractorActionsTest: MainViewInteractorActionsTest)
    fun inject(changeBitrateUCImplTest: ChangeBitrateUCTest)
    fun inject(changeSampleRateUCImplTest: ChangeSampleRateUCImplTest)
    fun inject(shareUCImplTest: ShareUCImplTest)
}