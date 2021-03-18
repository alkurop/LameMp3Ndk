package com.omar.retromp3recorder.app.ui.audio_controls

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class AudioControlsViewModel : ViewModel() {

    val state = MutableLiveData<AudioControlsView.State>()

    @Inject
    lateinit var interactor: AudioControlsInteractor

    private val actionPublishSubject = PublishSubject.create<AudioControlsView.Action>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)

        actionPublishSubject
            .compose(interactor.processActions())
            .compose(AudioControlsResultMapper.mapResultToState())
            .subscribe { state.postValue(it) }
            .disposedBy(compositeDisposable)
    }

    fun onAction(action: AudioControlsView.Action) {
        actionPublishSubject.onNext(action)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}