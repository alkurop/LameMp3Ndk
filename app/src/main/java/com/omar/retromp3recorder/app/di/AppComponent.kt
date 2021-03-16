package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.ui.main.MainActivity
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

    companion object {
        const val MAIN_THREAD = "MAIN_THREAD"
    }
}
