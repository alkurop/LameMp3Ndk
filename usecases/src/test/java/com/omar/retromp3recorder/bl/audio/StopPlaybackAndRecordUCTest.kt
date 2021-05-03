package com.omar.retromp3recorder.bl.audio

import com.nhaarman.mockitokotlin2.*
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateMapper
import com.omar.retromp3recorder.utils.FileLister
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class StopPlaybackAndRecordUCTest {
    @Inject
    lateinit var audioStateMapper: AudioStateMapper

    @Inject
    lateinit var player: AudioPlayer

    @Inject
    lateinit var recorder: Mp3VoiceRecorder

    @Inject
    lateinit var useCase: StopPlaybackAndRecordUC

    @Inject
    lateinit var fileLister: FileLister

    @Before
    fun setUp() {
        DaggerUseCaseComponent.create().inject(this)
    }

    @Test
    fun `stop play when playing`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Playing)

        useCase.execute().test().assertNoErrors().assertComplete()

        verify(player).playerStop()
        verifyZeroInteractions(recorder)
    }

    @Test
    fun `stop record when recording`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Recording)

        useCase.execute().test().assertNoErrors().assertComplete()

        verify(recorder).stopRecord()
        verifyZeroInteractions(player)
    }

    @Test
    fun `do nothing when idle`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Idle)

        useCase.execute().test().assertNoErrors().assertComplete()
        verifyZeroInteractions(player)
        verifyZeroInteractions(recorder)
    }

    @Test
    fun `look for files after stop`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Recording)

        useCase.execute().test()

        verify(fileLister).listFiles(any())
    }
}