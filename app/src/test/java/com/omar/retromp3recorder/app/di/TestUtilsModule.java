package com.omar.retromp3recorder.app.di;

import android.content.Context;

import org.mockito.Mockito;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

import static io.reactivex.schedulers.Schedulers.trampoline;

@Module
public class TestUtilsModule {

    @Provides
    Scheduler provideScheduler() {
        return trampoline();
    }

    @Provides
    Context context (){
        return Mockito.mock(Context.class);
    }

}
