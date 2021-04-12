package com.omar.retromp3recorder.app.di

import android.content.Context
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.files.FileEmptyChecker
import com.omar.retromp3recorder.files.FileLister
import com.omar.retromp3recorder.files.FilePathGenerator
import com.omar.retromp3recorder.utils.PermissionChecker
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

@Module
internal class TestUtilsModule {
    @Provides
    fun provideScheduler(): Scheduler {
        return Schedulers.trampoline()
    }

    @Provides
    fun context(): Context {
        return mock()
    }

    @Provides
    fun filePathGenerator(): FilePathGenerator {
        return mock<FilePathGenerator>().apply {
            whenever(this.generateFilePath()) doReturn "test"
            whenever(this.fileDir) doReturn "test"
        }
    }

    @Provides
    fun provideFileLister(): FileLister {
        return mock<FileLister>().apply {
            whenever(this.listFiles(any())) doReturn listOf("test")
        }
    }

    @Provides
    fun provideFileNonEmptyChecker(): FileEmptyChecker {
        return mock<FileEmptyChecker>().apply {
            whenever(this.isFileEmpty(any())) doReturn false
        }
    }

    @Provides
    fun providePermissionChecker(): PermissionChecker {
        return mock<PermissionChecker>().apply {
            whenever(this.showUnchecked(any())) doReturn emptySet()
        }
    }
}