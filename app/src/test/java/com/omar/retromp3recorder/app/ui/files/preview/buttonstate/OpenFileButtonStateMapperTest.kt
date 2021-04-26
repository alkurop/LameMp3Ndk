package com.omar.retromp3recorder.app.ui.files.preview.buttonstate

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateMapper
import com.omar.retromp3recorder.state.repos.FileListRepo
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import javax.inject.Inject

class OpenFileButtonStateMapperTest {
    @Mock
    lateinit var audioStateMapper: AudioStateMapper

    @Inject
    lateinit var fileListRepo: FileListRepo
    private lateinit var mapper: OpenFileButtonStateMapper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        DaggerTestAppComponent.create().inject(this)
        mapper = OpenFileButtonStateMapper(audioStateMapper, fileListRepo)
    }

    @Test
    fun `when audio recording state false`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Recording)

        mapper.observe().test().assertValue(false)
    }

    @Test
    fun `when audio playing state false`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Playing)

        mapper.observe().test().assertValue(false)
    }

    @Test
    fun `when audio idle and no files state false`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Idle(false))

        mapper.observe().test().assertValue(false)
    }

    @Test
    fun `when audio idle and has files state true`() {
        fileListRepo.onNext(listOf("something"))
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Idle(false))

        mapper.observe().test().assertValue(true)
    }
}