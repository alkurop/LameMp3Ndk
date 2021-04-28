package com.omar.retromp3recorder.di

import com.omar.retromp3recorder.bl.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestConfigModule::class,
        TestUtilsModuleUsecase::class,
        TestFunctionalityModule::class,
        TestRepoModule::class,
        TestUsecaseModule::class
    ]
)
interface UseCaseComponent {
    fun inject(changeBitrateUCImplTest: ChangeBitrateUCTest)
    fun inject(changeSampleRateUCImplTest: ChangeSampleRateUCImplTest)
    fun inject(shareUCImplTest: ShareUCImplTest)
    fun inject(takeLastFileUCTest: TakeLastFileUCTest)
    fun inject(stopPlaybackAndRecordUCTest: StopPlaybackAndRecordUCTest)
    fun inject(startRecordUCTest: StartRecordUCTest)
    fun inject(startPlaybackUCTest: StartPlaybackUCTest)
    fun inject(lookForFilesUCTest: LookForFilesUCTest)
    fun inject(checkPermissionsUCTest: CheckPermissionsUCTest)
    fun inject(setCurrentFileUCTest: SetCurrentFileUCTest)
    fun inject(deleteFileUCTest: DeleteFileUCTest)
    fun inject(loadRecorderSettingsUCTest: LoadRecorderSettingsUCTest)
}