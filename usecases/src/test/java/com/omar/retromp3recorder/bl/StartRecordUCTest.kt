package com.omar.retromp3recorder.bl

import com.nhaarman.mockitokotlin2.*
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.utils.FilePathGenerator
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.PermissionChecker
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class StartRecordUCTest {
    @Inject
    lateinit var startRecordUC: StartRecordUC

    @Inject
    lateinit var filePathGenerator: FilePathGenerator

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Inject
    lateinit var permissionChecker: PermissionChecker

    @Inject
    lateinit var voiceRecorder: Mp3VoiceRecorder

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
    }

    @Test
    fun `file path was updated after record started`() {
        val expectedFilepath = "expected_filepath"
        whenever(filePathGenerator.generateFilePath()) doReturn expectedFilepath

        startRecordUC.execute().test().assertNoErrors().assertComplete()

        currentFileRepo.observe().test().assertValue(Optional(expectedFilepath))
    }

    @Test
    fun `if permissions met record started`() {
        whenever(permissionChecker.showUnchecked(any())) doReturn emptySet()

        startRecordUC.execute().test().assertNoErrors().assertComplete()

        verify(voiceRecorder).record(any())
    }

    @Test
    fun `if permissions not met, record aborted`() {
        whenever(permissionChecker.showUnchecked(any())) doReturn setOf("test")

        startRecordUC.execute().test().assertNoErrors().assertComplete()

        verifyZeroInteractions(voiceRecorder)
    }
}