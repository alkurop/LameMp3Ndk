package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.main.MainActivity;
import com.omar.retromp3recorder.app.main.MainActivityV2;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = {
        ConfigModule.class,
        FunctionalityModule.class,
        UtilsModule.class
})
public interface AppComponent {

    String INTERNAL = "internal";

    void inject(MainActivity mainActivity);

    void inject(MainActivityV2 mainActivityV2);
}
