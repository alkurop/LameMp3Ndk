package com.omar.retromp3recorder.bl

import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.utils.FileEmptyChecker
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class TakeLastFileUCTest {

    @Inject
    lateinit var fileLRepo: FileListRepo

    @Inject
    lateinit var fileEmptyChecker: FileEmptyChecker

    @Inject
    lateinit var lookForFilesUC: LookForFilesUC

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
    }

    @Test
    fun `dont update if current file not empty`() {
    }

    @Test
    fun `update with null if last file empty`() {
    }

    @Test
    fun `update with last file `() {
    }
}