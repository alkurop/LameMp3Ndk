package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsViewModel
import com.omar.retromp3recorder.app.ui.main.MainActivity
import com.omar.retromp3recorder.app.ui.main.MainViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ConfigModule::class,
        FunctionalityModule::class,
        UtilsModule::class,
        UseCaseModule::class
    ]
)
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(viewModel: AudioControlsViewModel)
    fun inject(mainViewModel: MainViewModel)
}
