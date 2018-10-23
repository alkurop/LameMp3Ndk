package com.omar.retromp3recorder.app;

import android.app.Application;

import com.omar.retromp3recorder.app.di.AppComponent;
import com.omar.retromp3recorder.app.di.DaggerAppComponent;
import com.omar.retromp3recorder.app.di.UtilsModule;

/**
 * Created by omar on 20.08.15.
 */
public class App extends Application {

    public static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .builder()
                .utilsModule(
                        new UtilsModule(this)
                )
                .build();
    }
}
