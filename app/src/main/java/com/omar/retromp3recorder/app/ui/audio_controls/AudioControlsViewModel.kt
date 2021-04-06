package com.omar.retromp3recorder.app.ui.audio_controls

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsOutputMapper.mapOutputToState
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AudioControlsViewModel : ViewModel() {

    val state = BehaviorSubject.create<AudioControlsView.State>()

    @Inject
    lateinit var interactor: AudioControlsInteractor

    private val inputSubject = PublishSubject.create<AudioControlsView.Input>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        inputSubject
            .compose(interactor.processIO())
            .compose(mapOutputToState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    fun onInput(action: AudioControlsView.Input) {
        inputSubject.onNext(action)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}