package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsOutputMapper.mapOutputToState
import com.omar.retromp3recorder.state.repos.AudioState
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test

class AudioControlsOutputMapperTest {
    private val outputPublisher: Subject<AudioControlsView.Output> = PublishSubject.create()
    private lateinit var test: TestObserver<AudioControlsView.State>

    @Before
    fun setUp() {
        test = outputPublisher.compose(mapOutputToState()).test()
    }

    @Test
    fun `mapper starts with default view state`() {
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(1)
    }

    @Test
    fun `map audio state change result`() {
        val stateChangedResult = AudioControlsView.Output.AudioStateChanged(AudioState.Playing)

        //When
        outputPublisher.onNext(stateChangedResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (state) -> state === AudioState.Playing }
    }
}