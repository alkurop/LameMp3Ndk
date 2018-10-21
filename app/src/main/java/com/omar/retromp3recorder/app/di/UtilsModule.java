package com.omar.retromp3recorder.app.di;

import android.content.Context;

import com.omar.retromp3recorder.app.stringer.ContextStringProvider;
import com.omar.retromp3recorder.app.stringer.StringProvider;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

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
    Context context() {
        return context;
    }

    @Provides
    StringProvider providerStringer(Context context) {
        return new ContextStringProvider(context);
    }
}
