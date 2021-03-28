package com.omar.retromp3recorder.app.ui.visualizer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class VisualizerViewModel : ViewModel() {
    val state = MutableLiveData<VisualizerView.State>()

    @Inject
    lateinit var interactor: VisualizerInteractor

    private val inputSubject = PublishSubject.create<VisualizerView.Input>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        inputSubject
            .compose(interactor.processIO())
            .compose(VisualizerOutputMapper.mapOutputToState())
            .subscribe(state::postValue)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}