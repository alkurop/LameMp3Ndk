package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.files.FilesFragment
import com.omar.retromp3recorder.app.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ConfigModule::class, FunctionalityModule::class, UtilsModule::class, UseCaseModule::class])
interface AppComponent {
    fun inject(filesFragment: FilesFragment)
    fun inject(mainActivity: MainActivity)

    companion object {
        const val MAIN_THREAD = "MAIN_THREAD"
    }
}
