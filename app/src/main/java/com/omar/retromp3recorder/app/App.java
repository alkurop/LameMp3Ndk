package com.omar.retromp3recorder.app;

import android.app.Application;
import com.omar.retromp3recorder.app.utils.ContextHelper;

/**
 * Created by omar on 20.08.15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextHelper.setContext(this);
    }
}
