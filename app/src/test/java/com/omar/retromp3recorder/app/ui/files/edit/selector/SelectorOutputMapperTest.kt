package com.omar.retromp3recorder.app.ui.files.edit.selector

import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test

class SelectorOutputMapperTest {
    private val files = listOf("one", "two", "three")
    private val outputPublisher: Subject<SelectorView.Output> = PublishSubject.create()
    private lateinit var test: TestObserver<SelectorView.State>
    private val selection = files[1]
    private val anOtherOne = "anOtherOne"

    @Before
    fun setUp() {
        test = outputPublisher.compose(SelectorOutputMapper.mapOutputToState()).test()
        outputPublisher.onNext(SelectorView.Output.FileList(files))
        outputPublisher.onNext(SelectorView.Output.CurrentFile(selection))
    }

    @Test
    fun `on file list updated selected item was retained`() {
        outputPublisher.onNext(SelectorView.Output.FileList(files + anOtherOne))

        assert(test.values().last()!!.items.find { it.isCurrentItem }!!.filePath == selection)
    }

    @Test
    fun `on selected file changed, items in list were updated`() {
        outputPublisher.onNext(SelectorView.Output.FileList(files + anOtherOne))
        outputPublisher.onNext(SelectorView.Output.CurrentFile(anOtherOne))

        assert(test.values().last()!!.items.find { it.isCurrentItem }!!.filePath == anOtherOne)
    }
}