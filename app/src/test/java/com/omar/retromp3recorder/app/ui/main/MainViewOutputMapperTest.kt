package com.omar.retromp3recorder.app.ui.main

import com.omar.retromp3recorder.app.ui.main.MainView.State
import com.omar.retromp3recorder.app.ui.main.MainViewOutputMapper.mapOutputToState
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test

class MainViewOutputMapperTest {
    private val outputPublisher: Subject<MainView.Output> = PublishSubject.create()
    private lateinit var test: TestObserver<State>

    @Before
    fun setUp() {
        test = outputPublisher.compose(mapOutputToState()).test()
    }

    @Test
    fun `mapper starts with default view state`() {
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(1)
    }

    @Test
    fun `map request permission result`() {
        val permissionsToRequest = setOf("test")
        val requestPermissionsResult =
            MainView.Output.RequestPermissionsOutput(permissionsToRequest)

        //When
        outputPublisher.onNext(requestPermissionsResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(1)
            { (requestForPermissions) -> requestForPermissions.ghost === permissionsToRequest }
    }

}