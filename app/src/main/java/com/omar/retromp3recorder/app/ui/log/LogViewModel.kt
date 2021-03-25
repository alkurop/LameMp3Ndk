package com.omar.retromp3recorder.app.ui.log

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class LogViewModel : ViewModel() {

    val state = MutableLiveData<LogView.State>()

    @Inject
    lateinit var interactor: LogInteractor

    private val inputSubject = PublishSubject.create<LogView.Input>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        inputSubject
            .compose(interactor.processIO())
            .compose(LogOutputMapper.mapOutputToState())
            .subscribe(state::postValue)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }

}