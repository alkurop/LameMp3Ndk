package com.omar.retromp3recorder.app.di;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        ConfigModule.class,
        TestFunctionalityModule.class,
        TestUtilsModule.class
})
interface TestAppComponent {

}
