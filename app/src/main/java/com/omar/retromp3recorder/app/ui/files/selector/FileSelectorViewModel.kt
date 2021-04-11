package com.omar.retromp3recorder.app.ui.files.selector


import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class FileSelectorViewModel : ViewModel() {
    val state = BehaviorSubject.create<FileSelectorView.State>()

    @Inject
    lateinit var interactor: FileSelectorInteractor

    private val inputSubject = PublishSubject.create<FileSelectorView.Input>()
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        inputSubject
            .compose(interactor.processIO())
            .compose(FileSelectorOutputMapper.mapOutputToState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}