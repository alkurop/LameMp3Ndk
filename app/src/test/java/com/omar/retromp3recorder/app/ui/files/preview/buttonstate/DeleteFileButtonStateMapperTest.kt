package com.omar.retromp3recorder.app.ui.files.preview.buttonstate

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateMapper
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import javax.inject.Inject

class DeleteFileButtonStateMapperTest {
    @Mock
    lateinit var audioStateMapper: AudioStateMapper

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo
    private lateinit var mapper: DeleteFileButtonStateMapper

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        DaggerTestAppComponent.create().inject(this)
        mapper = DeleteFileButtonStateMapper(audioStateMapper, currentFileRepo)
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
    fun `when audio idle and no current file state false`() {
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Idle)

        mapper.observe().test().assertValue(false)
    }

    @Test
    fun `when audio idle and has current file state true`() {
        currentFileRepo.onNext(Optional("something"))
        whenever(audioStateMapper.observe()) doReturn Observable.just(AudioState.Idle)

        mapper.observe().test().assertValue(true)
    }
}