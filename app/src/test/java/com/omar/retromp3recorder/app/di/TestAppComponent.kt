package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsInteractorRepoTest
import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsInteractorTest
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
    fun inject(audioControlsInteractorTest: AudioControlsInteractorTest)
    fun inject(audioControlsInteractorRepoTest: AudioControlsInteractorRepoTest)
    fun inject(visualizerInteractorRepoTest: VisualizerInteractorRepoTest)
}