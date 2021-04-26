package com.omar.retromp3recorder.app.ui.audio_controls.buttonsstate

import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateMapper
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import com.omar.retromp3recorder.utils.FileEmptyChecker
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import javax.inject.Inject

class ShareButtonStateMapperTest {
    @Mock
    lateinit var fileEmptyChecker: FileEmptyChecker

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Mock
    lateinit var audioStateMapper: AudioStateMapper
    lateinit var mapper: ShareButtonStateMapper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        DaggerTestAppComponent.create().inject(this)
        mapper = ShareButtonStateMapper(currentFileRepo, audioStateMapper, fileEmptyChecker)
    }

    @Test
    fun `when audio recording state disabled`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Recording)

        mapper.observe().test().assertValue(InteractiveButton.State.DISABLED)
    }

    @Test
    fun `when audio playing state disabled`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Playing)

        mapper.observe().test().assertValue(InteractiveButton.State.DISABLED)
    }

    @Test
    fun `when audio idle and has file state enabled`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Idle)
        whenever(fileEmptyChecker.isFileEmpty(anyOrNull())) doReturn false

        mapper.observe().test().assertValue(InteractiveButton.State.ENABLED)
    }

    @Test
    fun `when audio idle and no file state disabled`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Idle)
        whenever(fileEmptyChecker.isFileEmpty(anyOrNull())) doReturn true

        mapper.observe().test().assertValue(InteractiveButton.State.DISABLED)
    }
}