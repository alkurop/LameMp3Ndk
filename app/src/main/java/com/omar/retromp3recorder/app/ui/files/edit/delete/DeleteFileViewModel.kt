package com.omar.retromp3recorder.app.ui.files.edit.delete

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class DeleteFileViewModel : ViewModel() {
    val state = BehaviorSubject.create<DeleteFileView.State>()
    val input = PublishSubject.create<DeleteFileView.Input>()

    @Inject
    lateinit var interactor: DeleteFileInteractor
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        input
            .compose(interactor.processIO())
            .compose(DeleteFileOutputMapper.mapOutputToState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}