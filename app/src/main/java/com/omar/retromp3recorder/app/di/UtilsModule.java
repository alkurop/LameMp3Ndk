package com.omar.retromp3recorder.app.di;

import android.content.Context;

import com.omar.retromp3recorder.app.stringer.ContextStringer;
import com.omar.retromp3recorder.app.stringer.Stringer;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

import static com.omar.retromp3recorder.app.di.AppComponent.MAIN_THREAD;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;
import static io.reactivex.schedulers.Schedulers.io;

@Module
public class UtilsModule {

    private final Context context;

    public UtilsModule(Context context) {
        this.context = context;
    }

    @Provides
    Scheduler provideScheduler() {
        return io();
    }

    @Provides
    @Named(MAIN_THREAD)
    Scheduler provideMainThreadScheduler(){
        return mainThread();
    }

    @Provides
    Context context() {
        return context;
    }

    @Provides
    Stringer providerStringer(Context context) {
        return new ContextStringer(context);
    }
}
