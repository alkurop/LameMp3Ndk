package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.common.usecase.ChangeBitrateUCTest
import com.omar.retromp3recorder.app.common.usecase.ChangeSampleRateUCImplTest
import com.omar.retromp3recorder.app.main.MainViewInteractorActionsTest
import com.omar.retromp3recorder.app.main.MainViewInteractorRepoTest
import com.omar.retromp3recorder.app.playback.player.LoggingAudioPlayerTest
import com.omar.retromp3recorder.app.playback.player.StateLoggingAudioPlayerTest
import com.omar.retromp3recorder.app.recording.recorder.LoggingVoiceRecorderTest
import com.omar.retromp3recorder.app.recording.recorder.StateLoggingVoiceRecorderTest
import com.omar.retromp3recorder.app.share.LoggingSharingModuleTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ConfigModule::class, TestFunctionalityModule::class, TestUtilsModule::class, MockModule::class])
interface TestAppComponent {
    fun inject(mainViewInteractorRepoTest: MainViewInteractorRepoTest)
    fun inject(mainViewInteractorActionsTest: MainViewInteractorActionsTest)
    fun inject(loggingAudioPlayerTest: LoggingAudioPlayerTest)
    fun inject(stateLoggingAudioPlayerTest: StateLoggingAudioPlayerTest)
    fun inject(loggingVoiceRecorderTest: LoggingVoiceRecorderTest)
    fun inject(stateLoggingVoiceRecorderTest: StateLoggingVoiceRecorderTest)
    fun inject(changeBitrateUCImplTest: ChangeBitrateUCTest)
    fun inject(changeSampleRateUCImplTest: ChangeSampleRateUCImplTest)
    fun inject(loggingSharingModuleTest: LoggingSharingModuleTest)
}