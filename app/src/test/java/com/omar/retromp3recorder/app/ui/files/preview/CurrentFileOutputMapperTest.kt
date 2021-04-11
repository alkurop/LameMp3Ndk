package com.omar.retromp3recorder.app.ui.files.preview

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class CurrentFileOutputMapperTest {
    @Inject
    lateinit var interactor: CurrentFileInteractor

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    private lateinit var test: TestObserver<CurrentFileView.Output>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = PublishSubject.create<CurrentFileView.Input>()
            .compose(interactor.processIO())
            .test()
    }

    @Test
    fun `interactor listens to current file repo`() {
        currentFileRepo.onNext(Optional("test1"))

        test.assertValueAt(0) { value ->
            value == CurrentFileView.Output.CurrentFileOutput(null)
        }
        test.assertValueAt(1) { value ->
            value == CurrentFileView.Output.CurrentFileOutput("test1")
        }
    }
}