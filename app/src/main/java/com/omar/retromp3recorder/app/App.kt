package com.omar.retromp3recorder.app

import android.app.Application
import com.akaita.java.rxjava2debug.RxJava2Debug
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.di.DaggerAppComponent
import com.omar.retromp3recorder.app.di.UtilsModule


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().utilsModule(UtilsModule(this)).build()
        RxJava2Debug.enableRxJava2AssemblyTracking(
            arrayOf("com.omar.retromp3recorder.app")
        )
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }
}