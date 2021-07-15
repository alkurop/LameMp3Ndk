package com.omar.retromp3recorder.app

import android.app.Application
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.di.DaggerAppComponent
import com.omar.retromp3recorder.app.di.UtilsModule
import com.omar.retromp3recorder.bl.WakeLockUsecase
import com.omar.retromp3recorder.bl.audio.PlayerProgressMapper
import com.omar.retromp3recorder.bl.files.NewFileUpdater
import com.omar.retromp3recorder.bl.files.TakeLastFileWithScanUC
import com.omar.retromp3recorder.bl.settings.LoadRecorderSettingsUC
import timber.log.Timber
import javax.inject.Inject

class App : Application() {
    @Inject
    lateinit var loadRecorderSettingsUC: LoadRecorderSettingsUC

    @Inject
    lateinit var cleanUpSeekRepoUsecase: NewFileUpdater

    @Inject
    lateinit var playerProgressMapper: PlayerProgressMapper

    @Inject
    lateinit var takeLastFileWithScanWithScanUC: TakeLastFileWithScanUC

    @Inject
    lateinit var wakelockUsecase: WakeLockUsecase

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().utilsModule(UtilsModule(this)).build()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        appComponent.inject(this)

        loadRecorderSettingsUC.execute().subscribe()
        takeLastFileWithScanWithScanUC.execute().subscribe()
        cleanUpSeekRepoUsecase.execute().subscribe()
        playerProgressMapper.execute().subscribe()
        wakelockUsecase.execute().subscribe()
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
    }
}