package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsOutputMapper.mapOutputToState
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
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
    fun `map play button state to play change`() {
        outputPublisher.onNext(AudioControlsView.Output.PlayButtonState(InteractiveButton.State.RUNNING))

        test.assertValueAt(
            1,
            AudioControlsView.State(playButtonState = InteractiveButton.State.RUNNING)
        )
    }

    @Test
    fun `map record button state to record change`() {
        outputPublisher.onNext(AudioControlsView.Output.RecordButtonState(InteractiveButton.State.RUNNING))

        test.assertValueAt(
            1,
            AudioControlsView.State(recordButtonState = InteractiveButton.State.RUNNING)
        )
    }

    @Test
    fun `map share button state to share change`() {
        outputPublisher.onNext(AudioControlsView.Output.ShareButtonState(InteractiveButton.State.RUNNING))

        test.assertValueAt(
            1,
            AudioControlsView.State(shareButtonState = InteractiveButton.State.RUNNING)
        )
    }

    @Test
    fun `map stop button state to stop change`() {
        outputPublisher.onNext(AudioControlsView.Output.StopButtonState(InteractiveButton.State.RUNNING))

        test.assertValueAt(
            1,
            AudioControlsView.State(stopButtonState = InteractiveButton.State.RUNNING)
        )
    }
}