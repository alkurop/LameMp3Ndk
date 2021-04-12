package com.omar.retromp3recorder.di

import com.omar.retromp3recorder.bl.ChangeBitrateUCTest
import com.omar.retromp3recorder.bl.ChangeSampleRateUCImplTest
import com.omar.retromp3recorder.bl.ShareUCImplTest
import com.omar.retromp3recorder.bl.TakeLastFileUCTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestConfigModule::class, TestUtilsModuleUsecase::class])
interface UseCaseComponent {
    fun inject(changeBitrateUCImplTest: ChangeBitrateUCTest)
    fun inject(changeSampleRateUCImplTest: ChangeSampleRateUCImplTest)
    fun inject(shareUCImplTest: ShareUCImplTest)
    fun inject(takeLastFileUCTest: TakeLastFileUCTest)
}