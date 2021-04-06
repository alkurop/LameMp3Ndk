package com.omar.retromp3recorder.app.ui.log

import com.github.alkurop.stringerbell.Stringer
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test

class LogOutputMapperTest {
    private val outputPublisher: Subject<LogView.Output> = PublishSubject.create()
    private lateinit var test: TestObserver<LogView.State>

    @Before
    fun setUp() {
        test = outputPublisher.compose(LogOutputMapper.mapOutputToState()).test()
    }

    @Test
    fun `map log repo message event`() {
        val messageLogResult = LogView.Output.MessageLogOutput(Stringer.ofString("test"))

        //When
        outputPublisher.onNext(messageLogResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (messages) ->
                val lastOrNull = messages.lastOrNull()!! as LogView.Output.MessageLogOutput
                Stringer.ofString("test") == lastOrNull.message
            }
    }

    @Test
    fun `map log repo error event`() {
        val errorLogResult = LogView.Output.ErrorLogOutput(Stringer.ofString("test"))

        //When
        outputPublisher.onNext(errorLogResult)

        test.values().forEach { println(it) }
        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (messages) ->
                val lastOrNull = messages.lastOrNull()!! as LogView.Output.ErrorLogOutput
                Stringer.ofString("test") == lastOrNull.error
            }
    }
}