package com.omar.retromp3recorder.bl

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.dto.toTestFileWrapper
import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.utils.FileLister
import com.omar.retromp3recorder.utils.FilePathGenerator
import org.junit.Before
import org.junit.Test
import java.io.File
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

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
    }

    @Test
    fun `files found are sent to FileListRepo`() {
        val filePath = "filePath"
        whenever(fileLister.listFiles(any())) doReturn listOf(File(filePath))

        lookForFilesUC.execute().test().assertNoErrors().assertComplete()

        fileListRepo.observe().test().assertValue(listOf(filePath.toTestFileWrapper()))
    }
}