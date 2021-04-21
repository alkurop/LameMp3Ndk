package com.omar.retromp3recorder.app.ui.files.edit.selector

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SelectorViewModel : ViewModel() {
    val state = BehaviorSubject.create<SelectorView.State>()
    val inputSubject = PublishSubject.create<SelectorView.Input>()

    @Inject
    lateinit var interactor: SelectorInteractor
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        inputSubject
            .compose(interactor.processIO())
            .compose(SelectorOutputMapper.mapOutputToState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}