package com.omar.retromp3recorder.app.ui.visualizer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class VisualizerViewModel : ViewModel() {
    val state = MutableLiveData<VisualizerView.State>()
    val input = PublishSubject.create<VisualizerView.Input>()

    @Inject
    lateinit var interactor: VisualizerInteractor
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        input
            .compose(interactor.processIO())
            .compose(VisualizerOutputMapper.mapOutputToState())
            .distinctUntilChanged()
            .subscribe(state::postValue)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}