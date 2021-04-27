package com.omar.retromp3recorder.app.ui.files.edit.delete

import com.nhaarman.mockitokotlin2.verify
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.dto.toTestFileWrapper
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.utils.FileDeleter
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class DeleteFileInteractorTest {
    @Inject
    lateinit var fileDeleter: FileDeleter

    @Inject
    lateinit var fileListRepo: FileListRepo

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Inject
    lateinit var interactor: DeleteFileInteractor
    private lateinit var test: TestObserver<DeleteFileView.Output>
    private val actionSubject: Subject<DeleteFileView.Input> = PublishSubject.create()
    private val filePath = "rama"

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = actionSubject.compose(interactor.processIO()).test()

        currentFileRepo.onNext(Optional(filePath))
        fileListRepo.onNext(listOf(filePath.toTestFileWrapper()))
    }

    @Test
    fun `on delete file input file was deleted`() {
        actionSubject.onNext(DeleteFileView.Input.DeleteFile(filePath))

        verify(fileDeleter).deleteFile(filePath)
    }

    @Test
    fun `on delete file finished output generated`() {
        actionSubject.onNext(DeleteFileView.Input.DeleteFile(filePath))

        test.assertNotComplete().assertNoErrors().assertValue(DeleteFileView.Output.Finished)
    }
}