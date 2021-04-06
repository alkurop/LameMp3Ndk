package com.omar.retromp3recorder.app.ui.recorder_settings.sample_rate

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SampleRateViewModel : ViewModel() {
    val state = BehaviorSubject.create<Mp3VoiceRecorder.SampleRate>()
    val inputSubject = PublishSubject.create<Mp3VoiceRecorder.SampleRate>()

    @Inject
    lateinit var interactor: SampleRateInteractor
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