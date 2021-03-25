package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsInteractorIOTest
import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsInteractorRepoTest
import com.omar.retromp3recorder.app.ui.log.LogInteractorRepoTest
import com.omar.retromp3recorder.app.ui.main.MainViewInteractorActionsTest
import com.omar.retromp3recorder.app.ui.main.MainViewInteractorRepoTest
import com.omar.retromp3recorder.app.ui.visualizer.VisualizerInteractorRepoTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ConfigModule::class, TestFunctionalityModule::class, TestUtilsModule::class, MockModule::class])
interface TestAppComponent {
    fun inject(mainViewInteractorRepoTest: MainViewInteractorRepoTest)
    fun inject(mainViewInteractorActionsTest: MainViewInteractorActionsTest)
    fun inject(audioControlsInteractorIOTest: AudioControlsInteractorIOTest)
    fun inject(audioControlsInteractorRepoTest: AudioControlsInteractorRepoTest)
    fun inject(visualizerInteractorRepoTest: VisualizerInteractorRepoTest)
    fun inject(logInteractorRepoTest: LogInteractorRepoTest)
}