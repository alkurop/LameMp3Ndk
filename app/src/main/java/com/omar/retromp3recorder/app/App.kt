package com.omar.retromp3recorder.app

import android.app.Application
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.di.DaggerAppComponent
import com.omar.retromp3recorder.app.di.UtilsModule
import com.omar.retromp3recorder.bl.files.LookForFilesUC
import com.omar.retromp3recorder.bl.settings.LoadRecorderSettingsUC
import timber.log.Timber
import javax.inject.Inject

class App : Application() {
    @Inject
    lateinit var loadRecorderSettingsUC: LoadRecorderSettingsUC

    @Inject
    lateinit var lookForFilesUC: LookForFilesUC

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().utilsModule(UtilsModule(this)).build()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        appComponent.inject(this)
        loadRecorderSettingsUC.execute().subscribe()
        lookForFilesUC.execute().subscribe()
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }
}