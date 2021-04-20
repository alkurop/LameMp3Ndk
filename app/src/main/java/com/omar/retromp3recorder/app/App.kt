package com.omar.retromp3recorder.app

import android.app.Application
import com.akaita.java.rxjava2debug.RxJava2Debug
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.di.DaggerAppComponent
import com.omar.retromp3recorder.app.di.UtilsModule
import timber.log.Timber


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().utilsModule(UtilsModule(this)).build()
        RxJava2Debug.enableRxJava2AssemblyTracking(
            arrayOf("com.omar.retromp3recorder.app")
        )
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    t?.let {
                        FirebaseCrashlytics.getInstance().recordException(it)
                    }
                }
            })
        }
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }
}