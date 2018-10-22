package com.omar.retromp3recorder.app.di;

import com.omar.retromp3recorder.app.main.MainViewResultMapperTest;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ConfigModule.class,
        TestFunctionalityModule.class,
        TestUtilsModule.class
})
public interface TestAppComponent {

    void inject(MainViewResultMapperTest mainViewPresenterTest);
}
