package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.utils.FileDeleter
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

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
    }

    @Test
    fun `file deleter was launched`() {
    }

    @Test
    fun `current file repo was updated if had deleted file`() {
    }

    @Test
    fun `current file repo was not updated if not had deleted file`() {
    }

    @Test
    fun `file list repo was updated`() {
    }
}