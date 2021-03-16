package com.omar.retromp3recorder.app.di

import android.content.Context
import com.omar.retromp3recorder.files_manipulation.FilePathGenerator
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.mockito.Mockito

@Module
internal class TestUtilsModule {
    @Provides
    fun provideScheduler(): Scheduler {
        return Schedulers.trampoline()
    }

    @Provides
    fun context(): Context {
        return Mockito.mock(Context::class.java)
    }

    @Provides
    fun filePathGenerator(): FilePathGenerator {
        return object : FilePathGenerator {
            override fun generateFilePath(): String {
                return "test"
            }

            override fun getFileDir(): String {
                return "test"
            }
        }
    }
}