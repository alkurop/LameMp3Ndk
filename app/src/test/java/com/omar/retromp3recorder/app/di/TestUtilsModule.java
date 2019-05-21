package com.omar.retromp3recorder.app.di;

import android.content.Context;

import com.omar.retromp3recorder.app.files.FilePathGenerator;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

import static io.reactivex.schedulers.Schedulers.trampoline;

@Module
class TestUtilsModule {

    @Provides
    Scheduler provideScheduler() {
        return trampoline();
    }

    @Provides
    Context context() {
        return Mockito.mock(Context.class);
    }

    @Provides
    FilePathGenerator filePathGenerator() {
        return new FilePathGenerator() {
            @Override
            public String generateFilePath() {
                return "test";
            }

            @Override
            public String getFileDir() {
                return "test";
            }
        };
    }

}
