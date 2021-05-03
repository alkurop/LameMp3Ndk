package com.omar.retromp3recorder.bl.files

import com.nhaarman.mockitokotlin2.verify
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.dto.toTestFileWrapper
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.utils.FileDeleter
import com.omar.retromp3recorder.utils.Optional
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class DeleteFileUCTest {
    @Inject
    lateinit var fileDeleter: FileDeleter

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Inject
    lateinit var fileListRepo: FileListRepo

    @Inject
    lateinit var fileDbEntityDao: FileDbEntityDao

    @Inject
    lateinit var useCase: DeleteFileUC
    private val testPath: String = "delete_file_test"

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
        currentFileRepo.onNext(Optional.empty())
        fileListRepo.onNext(listOf(testPath.toTestFileWrapper()))
    }

    @Test
    fun `file deleter was launched`() {
        useCase.execute(testPath).test().assertNoErrors().assertComplete()

        verify(fileDeleter).deleteFile(testPath)
    }

    @Test
    fun `current file repo was updated if had deleted file`() {
        currentFileRepo.onNext(Optional(testPath))
        useCase.execute(testPath).test().assertNoErrors().assertComplete()

        currentFileRepo.observe().test().assertValue(Optional.empty())
    }

    @Test
    fun `current file repo was not updated if not had deleted file`() {
        val otherPath = "some"
        currentFileRepo.onNext(Optional(otherPath))
        useCase.execute(testPath).test().assertNoErrors().assertComplete()

        currentFileRepo.observe().test().assertValue(Optional(otherPath))
    }

    @Test
    fun `file list repo was updated`() {
        fileListRepo.onNext(listOf(testPath.toTestFileWrapper()))
        useCase.execute(testPath).test().assertNoErrors().assertComplete()

        fileListRepo.observe().test().assertValue(emptyList())
    }

    @Test
    fun `on delete file record removed from db`() {
        useCase.execute(testPath).test().assertNoErrors().assertComplete()

        verify(fileDbEntityDao).deleteByFilepath(testPath)
    }
}