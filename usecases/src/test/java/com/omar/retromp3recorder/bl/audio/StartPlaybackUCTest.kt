package com.omar.retromp3recorder.bl.audio

import com.nhaarman.mockitokotlin2.*
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import com.omar.retromp3recorder.utils.PermissionChecker
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class StartPlaybackUCTest {
    @Inject
    lateinit var permissionChecker: PermissionChecker

    @Inject
    lateinit var audioPlayer: AudioPlayer

    @Inject
    lateinit var startPlaybackUC: StartPlaybackUC

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
    }

    @Test
    fun `when permission met playback started`() {
        currentFileRepo.onNext(Optional("file"))

        whenever(permissionChecker.showUnchecked(any())) doReturn emptySet()

        startPlaybackUC.execute().test().assertNoErrors().assertComplete()

        verify(audioPlayer).playerStart(any())
    }

    @Test
    fun `when permissions not met playback aborted`() {
        currentFileRepo.onNext(Optional("file"))
        whenever(permissionChecker.showUnchecked(any())) doReturn setOf("test")

        startPlaybackUC.execute().test().assertNoErrors().assertComplete()

        verifyZeroInteractions(audioPlayer)
    }

    @Test
    fun `when CurrentFileRepo empty should crash with NPE`() {
        currentFileRepo.onNext(Optional(null))

        whenever(permissionChecker.showUnchecked(any())) doReturn emptySet()

        startPlaybackUC.execute().test().assertError(NullPointerException::class.java)
    }
}