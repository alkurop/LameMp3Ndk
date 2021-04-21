package com.omar.retromp3recorder.app.ui.files.edit.selector

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SelectorInteractorInputTest {
    @Inject
    lateinit var interactor: SelectorInteractor

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo
    private lateinit var test: TestObserver<SelectorView.Output>
    private val actionSubject: Subject<SelectorView.Input> = PublishSubject.create()

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = actionSubject.compose(interactor.processIO()).test()
    }

    @Test
    fun `current file input sets current file`() {
        val fileName = "horosho"
        val item = SelectorView.Item(fileName, false)
        actionSubject.onNext(SelectorView.Input.ItemSelected(item))

        currentFileRepo.observe().test().assertValue(Optional(fileName))
    }
}