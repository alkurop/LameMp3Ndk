package com.omar.retromp3recorder.app.di

import android.content.Context
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.utils.*
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import javax.inject.Singleton

@Module
class TestUtilsModule {
    @Singleton
    @Provides
    fun provideScheduler(): Scheduler {
        return Schedulers.trampoline()
    }

    @Singleton
    @Provides
    fun context(): Context {
        return mock()
    }

    @Singleton
    @Provides
    fun filePathGenerator(): FilePathGenerator {
        return mock<FilePathGenerator>().apply {
            whenever(this.generateFilePath()) doReturn "test"
            whenever(this.fileDir) doReturn "test"
        }
    }

    @Singleton
    @Provides
    fun provideFileLister(): FileLister {
        return mock<FileLister>().apply {
            whenever(this.listFiles(any())) doReturn listOf(File("test"))
        }
    }

    @Singleton
    @Provides
    fun provideFileNonEmptyChecker(): FileEmptyChecker {
        return mock<FileEmptyChecker>().apply {
            whenever(this.isFileEmpty(any())) doReturn false
        }
    }

    @Singleton
    @Provides
    fun providePermissionChecker(): PermissionChecker {
        return mock<PermissionChecker>().apply {
            whenever(this.showUnchecked(any())) doReturn emptySet()
        }
    }

    @Singleton
    @Provides
    fun provideFileDeleter(): FileDeleter {
        return mock<FileDeleter>()
    }
}