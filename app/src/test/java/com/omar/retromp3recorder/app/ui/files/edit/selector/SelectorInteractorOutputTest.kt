package com.omar.retromp3recorder.app.ui.files.edit.selector

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SelectorInteractorOutputTest {
    @Inject
    lateinit var interactor: SelectorInteractor

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Inject
    lateinit var fileListRepo: FileListRepo
    private lateinit var test: TestObserver<SelectorView.Output>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = PublishSubject.create<SelectorView.Input>()
            .compose(interactor.processIO())
            .test()
    }

    @Test
    fun `interactor listens to CurrentFileRepo`() {
        val fileName = "otlichno"
        currentFileRepo.onNext(Optional(fileName))

        test.assertValueAt(1, SelectorView.Output.CurrentFile(fileName))
    }

    @Test
    fun `interactor listens to FileListRepo`() {
        val files = listOf("zdraste")
        fileListRepo.onNext(files)

        test.assertValueAt(1, SelectorView.Output.FileList(files))
    }
}