package com.omar.retromp3recorder.app.ui.recorder_settings.beat_rate

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class BitRateSettingsViewModel : ViewModel() {
    val state = BehaviorSubject.create<Mp3VoiceRecorder.BitRate>()
    val inputSubject = PublishSubject.create<Mp3VoiceRecorder.BitRate>()

    @Inject
    lateinit var interactor: BitRateSettingsInteractor
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        inputSubject
            .compose(interactor.processIO())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}