package com.omar.retromp3recorder.di

import com.omar.retromp3recorder.bl.ChangeBitrateUCTest
import com.omar.retromp3recorder.bl.ChangeSampleRateUCImplTest
import com.omar.retromp3recorder.bl.ShareUCImplTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [TestConfigModule::class])
interface UseCaseComponent {
    fun inject(changeBitrateUCImplTest: ChangeBitrateUCTest)
    fun inject(changeSampleRateUCImplTest: ChangeSampleRateUCImplTest)
    fun inject(shareUCImplTest: ShareUCImplTest)
}