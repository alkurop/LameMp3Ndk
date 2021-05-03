package com.omar.retromp3recorder.bl.files

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.dto.toTestFileWrapper
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.FileListRepo
import com.omar.retromp3recorder.utils.FileEmptyChecker
import com.omar.retromp3recorder.utils.Optional
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class TakeLastFileUCTest {
    @Inject
    lateinit var fileListRepo: FileListRepo

    @Inject
    lateinit var fileEmptyChecker: FileEmptyChecker

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Inject
    lateinit var useCase: TakeLastFileUC

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
        fileListRepo.onNext(listOf("test1".toTestFileWrapper()))
    }

    @Test
    fun `dont update if current file not empty`() {
        fileListRepo.onNext(listOf("no good".toTestFileWrapper()))
        currentFileRepo.onNext(Optional("test"))

        useCase.execute().test().assertComplete()

        currentFileRepo.observe().test().assertValue(Optional("test"))
    }

    @Test
    fun `update with null if last file empty`() {
        whenever(fileEmptyChecker.isFileEmpty(any())) doReturn true
        fileListRepo.onNext(listOf("test".toTestFileWrapper()))
        currentFileRepo.onNext(Optional(null))

        useCase.execute().test().assertComplete()

        currentFileRepo.observe().test().assertValue(Optional(null))
    }

    @Test
    fun `update with last file `() {
        fileListRepo.onNext(listOf("test".toTestFileWrapper()))
        currentFileRepo.onNext(Optional(null))

        useCase.execute().test().assertComplete()

        currentFileRepo.observe().test().assertValue(Optional("test"))
    }
}