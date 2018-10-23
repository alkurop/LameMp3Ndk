package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;
@Singleton
@Component(modules = {
        ConfigModule.class,
        FunctionalityModule.class,
        UtilsModule.class
})
public interface AppComponent {

    String DECORATOR_ALPHA = "DECORATOR_ALPHA";
    String DECORATOR_BETA = "DECORATOR_BETA";
    String MAIN_THREAD = "MAIN_THREAD";

    void inject(MainActivity mainActivity);

}
