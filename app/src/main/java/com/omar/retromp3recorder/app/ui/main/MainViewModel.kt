package com.omar.retromp3recorder.app.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.ui.main.MainViewOutputMapper.mapOutputToState
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class MainViewModel : ViewModel() {

    val state = MutableLiveData<MainView.State>()

    @Inject
    lateinit var interactor: MainViewInteractor

    private val inputSubject = PublishSubject.create<MainView.Input>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        inputSubject
            .compose(interactor.processIO())
            .compose(mapOutputToState())
            .subscribe(state::postValue)
            .disposedBy(compositeDisposable)
    }

    fun onInput(action: MainView.Input) {
        inputSubject.onNext(action)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}
