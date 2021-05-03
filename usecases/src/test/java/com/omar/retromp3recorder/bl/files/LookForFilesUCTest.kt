package com.omar.retromp3recorder.bl.files

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.dto.toTestFileWrapper
import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.storage.db.FileDbEntityDao
import com.omar.retromp3recorder.storage.db.toDatabaseEntity
import com.omar.retromp3recorder.utils.FileLister
import com.omar.retromp3recorder.utils.FilePathGenerator
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class LookForFilesUCTest {
    @Inject
    lateinit var fileListRepo: FileListRepo

    @Inject
    lateinit var filePathGenerator: FilePathGenerator

    @Inject
    lateinit var fileLister: FileLister

    @Inject
    lateinit var lookForFilesUC: LookForFilesUC

    @Inject
    lateinit var fileDbEntityDao: FileDbEntityDao

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
    }

    @Test
    fun `files found are sent to FileListRepo`() {
        val file = "filePath".toTestFileWrapper()
        whenever(fileLister.listFiles(any())) doReturn listOf(file)

        lookForFilesUC.execute().test().assertNoErrors().assertComplete()

        fileListRepo.observe().test().assertValue(listOf(file))
    }

    @Test
    fun `new files added to db`() {
        val newFile = "newFile".toTestFileWrapper()

        whenever(fileLister.listFiles(any())) doReturn listOf(newFile)

        lookForFilesUC.execute().test().assertNoErrors().assertComplete()

        verify(fileDbEntityDao).insert(listOf(newFile.toDatabaseEntity()))
    }

    @Test
    fun `unresolved files removed from db`() {
        val newFile = "newFile".toTestFileWrapper()

        whenever(fileLister.listFiles(any())) doReturn emptyList()

        whenever(fileDbEntityDao.getAll()) doReturn listOf(newFile.toDatabaseEntity())

        lookForFilesUC.execute().test().assertNoErrors().assertComplete()

        verify(fileDbEntityDao).delete(listOf(newFile.toDatabaseEntity()))
    }
}