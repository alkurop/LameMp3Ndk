package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsInteractorInputTest
import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsInteractorOutputTest
import com.omar.retromp3recorder.app.ui.files.preview.CurrentFileInteractorInputTest
import com.omar.retromp3recorder.app.ui.files.preview.CurrentFileInteractorOutputTest
import com.omar.retromp3recorder.app.ui.log.LogInteractorRepoTest
import com.omar.retromp3recorder.app.ui.main.MainViewInteractorRepoTest
import com.omar.retromp3recorder.app.ui.recorder_settings.beat_rate.BitRateSettingsInteractorInputTest
import com.omar.retromp3recorder.app.ui.recorder_settings.beat_rate.BitRateSettingsInteractorOutputTest
import com.omar.retromp3recorder.app.ui.recorder_settings.sample_rate.SampleRateInteractorInputTest
import com.omar.retromp3recorder.app.ui.recorder_settings.sample_rate.SampleRateInteractorOutputTest
import com.omar.retromp3recorder.app.ui.visualizer.VisualizerInteractorOutputTest
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ConfigModule::class,
        MockModule::class,
        RepoModule::class,
        TestFunctionalityModule::class,
        TestUtilsModule::class
    ]
)
interface TestAppComponent {
    fun inject(mainViewInteractorRepoTest: MainViewInteractorRepoTest)
    fun inject(audioControlsInteractorInputTest: AudioControlsInteractorInputTest)
    fun inject(audioControlsInteractorRepoTest: AudioControlsInteractorOutputTest)
    fun inject(visualizerInteractorRepoTest: VisualizerInteractorOutputTest)
    fun inject(logInteractorRepoTest: LogInteractorRepoTest)
    fun inject(bitRateSettingsInteractorInputTest: BitRateSettingsInteractorInputTest)
    fun inject(sampleRateInteractorInputTest: SampleRateInteractorInputTest)
    fun inject(sampleRateInteractorOutputTest: SampleRateInteractorOutputTest)
    fun inject(bitRateSettingsInteractorOutputTest: BitRateSettingsInteractorOutputTest)
    fun inject(currentFileInteractorInputTest: CurrentFileInteractorInputTest)
    fun inject(currentFileOutputMapperTest: CurrentFileInteractorOutputTest)
}