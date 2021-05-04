package com.omar.retromp3recorder.app.ui.audio_controls

import com.google.common.truth.Truth.assertThat
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class AudioControlsInteractorOutputTest {
    @Inject
    lateinit var audioStateMapper: AudioStateMapper

    @Inject
    lateinit var interactor: AudioControlsInteractor

    @Inject
    lateinit var audioPlayer: AudioPlayer
    private lateinit var test: TestObserver<AudioControlsView.Output>

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = PublishSubject.create<AudioControlsView.Input>()
            .compose(interactor.processIO())
            .test()
        currentFileRepo.onNext(Optional("test"))
    }

    @Test
    fun `interactor listens to play button state mapper`() {
        audioPlayer.playerStart("test")

        assertThat(test.values()).contains(
            AudioControlsView.Output.PlayButtonState(
                InteractiveButton.State.RUNNING
            )
        )
    }

    @Test
    fun `interactor listens to stop button state mapper`() {
        audioPlayer.playerStart("test")

        assertThat(test.values()).contains(
            AudioControlsView.Output.StopButtonState(
                InteractiveButton.State.ENABLED
            )
        )
    }

    @Test
    fun `interactor listens to record button state mapper`() {
        audioPlayer.playerStart("test")

        assertThat(test.values()).contains(
            AudioControlsView.Output.RecordButtonState(
                InteractiveButton.State.DISABLED
            )
        )
    }

    @Test
    fun `interactor listens to share button state mapper`() {
        assertThat(test.values()).contains(
            AudioControlsView.Output.ShareButtonState(
                InteractiveButton.State.ENABLED
            )
        )
    }
}