package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsViewModel
import com.omar.retromp3recorder.app.ui.files.preview.CurrentFileViewModel
import com.omar.retromp3recorder.app.ui.files.selector.FileSelectorViewModel
import com.omar.retromp3recorder.app.ui.log.LogViewModel
import com.omar.retromp3recorder.app.ui.main.MainViewModel
import com.omar.retromp3recorder.app.ui.recorder_settings.beat_rate.BitRateSettingsViewModel
import com.omar.retromp3recorder.app.ui.recorder_settings.sample_rate.SampleRateViewModel
import com.omar.retromp3recorder.app.ui.visualizer.VisualizerViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ConfigModule::class,
        FunctionalityModule::class,
        UtilsModule::class,
    ]
)
interface AppComponent {
    fun inject(viewModel: AudioControlsViewModel)
    fun inject(mainViewModel: MainViewModel)
    fun inject(visualizerViewModel: VisualizerViewModel)
    fun inject(logViewModel: LogViewModel)
    fun inject(bitRateSettingsViewModel: BitRateSettingsViewModel)
    fun inject(sampleRateViewModel: SampleRateViewModel)
    fun inject(currentFileViewModel: CurrentFileViewModel)
    fun inject(fileSelectorViewModel: FileSelectorViewModel)
}
