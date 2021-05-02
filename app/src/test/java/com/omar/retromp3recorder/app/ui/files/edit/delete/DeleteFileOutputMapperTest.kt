package com.omar.retromp3recorder.app.ui.files.edit.delete

import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import org.junit.Before
import org.junit.Test

class DeleteFileOutputMapperTest {
    private lateinit var test: TestObserver<DeleteFileView.State>
    private val outputPublisher: Subject<DeleteFileView.Output> = PublishSubject.create()

    @Before
    fun setUp() {
        test = outputPublisher.compose(DeleteFileOutputMapper.mapOutputToState()).test()
    }

    @Test
    fun `starts with dismiss false`() {
        test.assertValue(DeleteFileView.State(shouldDismiss = false))
    }

    @Test
    fun `on finished output dismiss true`() {
        outputPublisher.onNext(DeleteFileView.Output.Finished)

        test.assertValueAt(1, DeleteFileView.State(shouldDismiss = true))
    }
}