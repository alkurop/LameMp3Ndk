package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsViewModel
import com.omar.retromp3recorder.app.ui.files.edit.delete.DeleteFileViewModel
import com.omar.retromp3recorder.app.ui.files.edit.properties.PropertiesViewModel
import com.omar.retromp3recorder.app.ui.files.edit.rename.CanRenameFileComponent
import com.omar.retromp3recorder.app.ui.files.edit.selector.SelectorViewModel
import com.omar.retromp3recorder.app.ui.files.preview.CurrentFileViewModel
import com.omar.retromp3recorder.app.ui.log.LogViewModel
import com.omar.retromp3recorder.app.ui.main.MainViewModel
import com.omar.retromp3recorder.app.ui.recorder_settings.beat_rate.BitRateSettingsViewModel
import com.omar.retromp3recorder.app.ui.recorder_settings.sample_rate.SampleRateViewModel
import com.omar.retromp3recorder.app.ui.visualizer.VisualizerViewModel
import com.omar.retromp3recorder.storage.StorageModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ConfigModule::class,
        FunctionalityModule::class,
        RepoModule::class,
        UtilsModule::class,
        StorageModule::class
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
    fun inject(fileSelectorViewModel: SelectorViewModel)
    fun inject(deleteFileViewModel: DeleteFileViewModel)
    fun inject(app: App)

    fun canRenameFileComponent(): CanRenameFileComponent
    fun inject(propertiesViewModel: PropertiesViewModel)
}
