package com.omar.retromp3recorder.app.di

import android.app.Application
import android.content.Context
import com.omar.retromp3recorder.files_manipulation.FilePathGenerator
import com.omar.retromp3recorder.files_manipulation.FilePathGeneratorImpl
import com.omar.retromp3recorder.utils.MAIN_THREAD
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class UtilsModule(private val app: Application) {
    @Provides
    fun provideScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Named(MAIN_THREAD)
    fun provideMainThreadScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    fun context(): Context {
        return app
    }

    @Provides
    fun filePathGenerator(context: Context): FilePathGenerator {
        return FilePathGeneratorImpl(context)
    }
}
