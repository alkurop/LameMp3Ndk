package com.omar.retromp3recorder.app.ui.audio_controls

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateRepo
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class AudioControlsInteractorOutputTest {
    @Inject
    lateinit var audioStateRepo: AudioStateRepo

    @Inject
    lateinit var interactor: AudioControlsInteractor

    @Inject
    lateinit var audioPlayer: AudioPlayer
    private lateinit var test: TestObserver<AudioControlsView.Output>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = PublishSubject.create<AudioControlsView.Input>()
            .compose(interactor.processIO())
            .test()
    }

    @Test
    fun `interactor listens to audio state repo`() {
        audioPlayer.playerStart("test")
        println(test.values())
        test.assertValueAt(0) { value ->
            value == AudioControlsView.Output.AudioStateChanged(AudioState.Idle(true))
        }
        test.assertValueAt(1) { value ->
            value == AudioControlsView.Output.AudioStateChanged(AudioState.Playing)
        }
    }

}