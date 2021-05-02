package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateMapper
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class StopButtonStateMapperTest {
    @Mock
    lateinit var audioStateMapper: AudioStateMapper
    lateinit var mapper: StopButtonStateMapper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mapper = StopButtonStateMapper(audioStateMapper)
    }

    @Test
    fun `audio recording state enabled`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Recording)

        mapper.observe().test().assertValue(InteractiveButton.State.ENABLED)
    }

    @Test
    fun `audio playing state enabled`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Playing)

        mapper.observe().test().assertValue(InteractiveButton.State.ENABLED)
    }

    @Test
    fun `audio idle state disabled`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Idle)

        mapper.observe().test().assertValue(InteractiveButton.State.DISABLED)
    }
}