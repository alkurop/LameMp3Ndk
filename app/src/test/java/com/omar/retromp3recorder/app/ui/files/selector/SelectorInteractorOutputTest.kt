package com.omar.retromp3recorder.app.ui.files.selector

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.dto.toFutureFileWrapper
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.FileListRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.PublishSubject
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

        test.assertValueAt(2, SelectorView.Output.CurrentFile(fileName))
    }

    @Test
    fun `interactor listens to FileListRepo`() {
        val files = listOf(
            "zdraste".toFutureFileWrapper()
        )
        fileListRepo.onNext(files)

        test.assertValueAt(2, SelectorView.Output.FileList(files))
    }
}