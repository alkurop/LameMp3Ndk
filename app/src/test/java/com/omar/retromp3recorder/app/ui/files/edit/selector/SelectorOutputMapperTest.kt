package com.omar.retromp3recorder.app.ui.files.edit.selector

import com.omar.retromp3recorder.dto.toTestFileWrapper
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import org.junit.Before
import org.junit.Test

class SelectorOutputMapperTest {
    private val files = listOf("one", "two", "three").map { it.toTestFileWrapper() }
    private val outputPublisher: Subject<SelectorView.Output> = PublishSubject.create()
    private lateinit var test: TestObserver<SelectorView.State>
    private val selection = files[1]
    private val anOtherOne = "anOtherOne".toTestFileWrapper()

    @Before
    fun setUp() {
        test = outputPublisher.compose(SelectorOutputMapper.mapOutputToState()).test()
        outputPublisher.onNext(SelectorView.Output.FileList(files))
        outputPublisher.onNext(SelectorView.Output.CurrentFile(selection.path))
    }

    @Test
    fun `on file list updated selected item was retained`() {
        outputPublisher.onNext(SelectorView.Output.FileList(files + anOtherOne))

        assert(
            test.values()
                .last()!!.items.find { it.isCurrentItem }!!.fileWrapper.path == selection.path
        )
    }

    @Test
    fun `on selected file changed, items in list were updated`() {
        outputPublisher.onNext(SelectorView.Output.FileList(files + anOtherOne))
        outputPublisher.onNext(SelectorView.Output.CurrentFile(anOtherOne.path))
        val lastState = test.values().last()!!
        val selectedItem = lastState.items.find { it.isCurrentItem }!!

        assert(selectedItem.fileWrapper.path == anOtherOne.path)
        assert(lastState.selectedFile == anOtherOne.path)
    }
}