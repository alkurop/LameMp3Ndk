package com.omar.retromp3recorder.di

import com.omar.retromp3recorder.bl.CheckPermissionsUCTest
import com.omar.retromp3recorder.bl.ShareUCImplTest
import com.omar.retromp3recorder.bl.audio.StartPlaybackUCTest
import com.omar.retromp3recorder.bl.audio.StartRecordUCTest
import com.omar.retromp3recorder.bl.audio.StopPlaybackAndRecordUCTest
import com.omar.retromp3recorder.bl.files.*
import com.omar.retromp3recorder.bl.settings.ChangeBitrateUCTest
import com.omar.retromp3recorder.bl.settings.ChangeSampleRateUCImplTest
import com.omar.retromp3recorder.bl.settings.LoadRecorderSettingsUCTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestConfigModule::class,
        TestUtilsModuleUsecase::class,
        TestFunctionalityModule::class,
        TestRepoModule::class,
        TestStorageModule::class
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
    fun inject(canRenameFileUCTest: CanRenameFileUCTest)
    fun inject(renameFileUCTest: RenameFileUCTest)
}