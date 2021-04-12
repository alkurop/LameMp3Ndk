package com.omar.retromp3recorder.app.ui.files.preview

import com.google.common.truth.Truth.assertThat
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class CurrentFileInteractorInputTest {
    @Inject
    lateinit var interactor: CurrentFileInteractor

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    private lateinit var test: TestObserver<CurrentFileView.Output>

    private val actionSubject: Subject<CurrentFileView.Input> = PublishSubject.create()

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = actionSubject.compose(interactor.processIO()).test()
    }

    @Test
    fun `file lookup leads to usecase execution and current file is 'test'`() {
        //When
        actionSubject.onNext(CurrentFileView.Input.LookForPlayableFile)

        //Then
        // assertThat()

        assertThat(currentFileRepo.observe().blockingFirst().value).isEqualTo("test")

    }
}