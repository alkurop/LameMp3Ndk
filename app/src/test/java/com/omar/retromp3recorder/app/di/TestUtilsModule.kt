package com.omar.retromp3recorder.app.di

import android.content.Context
import com.omar.retromp3recorder.files.FileEmptyChecker
import com.omar.retromp3recorder.files.FileLister
import com.omar.retromp3recorder.files.FilePathGenerator
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

            override val fileDir: String
                get() = "test"
        }
    }

    @Provides
    fun provideFileLister(): FileLister {
        return object : FileLister {
            override fun listFiles(dirPath: String): List<String> {
                return listOf("test")
            }
        }
    }

    @Provides
    fun provideFileNonEmptyChecker(): FileEmptyChecker {
        return object : FileEmptyChecker {
            override fun isFileEmpty(path: String?): Boolean {
                return path != "test"
            }
        }
    }
}