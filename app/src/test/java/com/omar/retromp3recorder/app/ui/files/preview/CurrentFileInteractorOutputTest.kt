package com.omar.retromp3recorder.app.ui.files.preview

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.R
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test

class CurrentFileInteractorOutputTest {
    private val outputPublisher: Subject<CurrentFileView.Output> = PublishSubject.create()
    private lateinit var test: TestObserver<CurrentFileView.State>

    @Before
    fun setUp() {
        test = outputPublisher.compose(CurrentFileOutputMapper.mapOutputToState()).test()
    }

    @Test
    fun `mapper starts with default view state`() {
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValueAt(0) { value ->
                value == CurrentFileView.State(Stringer(R.string.no_file))
            }
    }

    @Test
    fun `map file output change result`() {
        val stateChangedResult = CurrentFileView.Output.CurrentFileOutput("la")

        //When
        outputPublisher.onNext(stateChangedResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(1) { value ->
                value == CurrentFileView.State(Stringer.ofString("la"))
            }
    }
}