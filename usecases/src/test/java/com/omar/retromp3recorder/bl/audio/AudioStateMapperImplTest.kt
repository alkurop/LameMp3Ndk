package com.omar.retromp3recorder.bl.audio

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.FileEmptyChecker
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

internal class AudioStateMapperImplTest {
    @Mock
    lateinit var player: AudioPlayer

    @Mock
    lateinit var recorder: Mp3VoiceRecorder

    @Mock
    lateinit var fileEmptyChecker: FileEmptyChecker
    lateinit var audioStateMapper: AudioStateMapper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        audioStateMapper = AudioStateMapperImpl(
            recorder = recorder,
            player = player
        )
        whenever(fileEmptyChecker.isFileEmpty(any())) doReturn false
    }

    @Test
    fun `when player is playing state playing`() {
        whenever(player.observeState()) doReturn Observable.just(AudioPlayer.State.Playing)
        whenever(recorder.observeState()) doReturn Observable.just(Mp3VoiceRecorder.State.Idle)

        audioStateMapper.observe().test()
            .assertValue(AudioState.Playing)
    }

    @Test
    fun `when recorder is recording state recording`() {
        whenever(player.observeState()) doReturn Observable.just(AudioPlayer.State.Idle)
        whenever(recorder.observeState()) doReturn Observable.just(Mp3VoiceRecorder.State.Recording)

        audioStateMapper.observe().test()
            .assertValue(AudioState.Recording)
    }

    @Test
    fun `when both idle state Idle`() {
        whenever(player.observeState()) doReturn Observable.just(AudioPlayer.State.Idle)
        whenever(recorder.observeState()) doReturn Observable.just(Mp3VoiceRecorder.State.Idle)

        audioStateMapper.observe().test()
            .assertValue(AudioState.Idle)
    }
}