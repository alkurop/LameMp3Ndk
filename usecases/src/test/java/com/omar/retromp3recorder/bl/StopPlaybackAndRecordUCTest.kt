package com.omar.retromp3recorder.bl

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.di.DaggerUseCaseComponent
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateMapper
import io.reactivex.Observable
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
}