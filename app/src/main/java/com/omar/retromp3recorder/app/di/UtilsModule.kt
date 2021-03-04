package com.omar.retromp3recorder.app.di

import android.content.Context
import com.omar.retromp3recorder.app.files.FilePathGenerator
import com.omar.retromp3recorder.app.files.FilePathGeneratorImpl
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class UtilsModule(private val context: Context) {
    @Provides
    fun provideScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Named(AppComponent.MAIN_THREAD)
    fun provideMainThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    fun context(): Context {
        return context
    }

    @Provides
    fun filePathGenerator(context: Context): FilePathGenerator {
        return FilePathGeneratorImpl(context)
    }
}
