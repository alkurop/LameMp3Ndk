package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.files.FilesFragment;
import com.omar.retromp3recorder.app.main.MainActivity;

import org.jetbrains.annotations.NotNull;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ConfigModule.class,
        FunctionalityModule.class,
        UtilsModule.class,
        UseCaseModule.class
})
public interface AppComponent {

    String DECORATOR_A = "DECORATOR_A";
    String DECORATOR_B = "DECORATOR_B";
    String MAIN_THREAD = "MAIN_THREAD";


    void inject(@NotNull FilesFragment filesFragment);

    void inject(@NotNull MainActivity mainActivity);
}
