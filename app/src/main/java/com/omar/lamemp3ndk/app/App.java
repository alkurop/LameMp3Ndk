package com.omar.lamemp3ndk.app;

import android.app.Application;
import com.omar.lamemp3ndk.app.utils.ContextHelper;

/**
 * Created by omar on 20.08.15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextHelper.SetContext(this);
    }
}
