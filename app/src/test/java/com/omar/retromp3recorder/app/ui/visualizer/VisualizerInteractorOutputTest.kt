package com.omar.retromp3recorder.app.ui.visualizer

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.modules.TestAudioPlayer.Companion.PLAYER_ID
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.state.repos.AudioState
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class VisualizerInteractorOutputTest {

    @Inject
    lateinit var audioPlayer: AudioPlayer

    @Inject
    lateinit var interactor: VisualizerInteractor

    private lateinit var test: TestObserver<VisualizerView.Output>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = PublishSubject.create<VisualizerView.Input>()
            .compose(interactor.processIO())
            .test()
    }


    @Test
    fun `interactor listens to player id repo`() {
        //When
        audioPlayer.playerStart("test")

        //Then
        test.assertValueAt(
            PLAYERID_SUBSCRIPTION_INDEX
        ) { output: VisualizerView.Output ->
            val playerId: Int = (output as VisualizerView.Output.PlayerIdOutput).playerId
            playerId == PLAYER_ID
        }
    }

    @Test
    fun `interactor listens to state repo`() {
        //When
        audioPlayer.playerStart("test")
        //Then

        test.assertValueAt(
            AUDIOSTATE_SUBSRIPTION_INDEX
        ) { output: VisualizerView.Output ->
            val state = (output as VisualizerView.Output.AudioStateChanged).state
            state == AudioState.Playing
        }
    }
}

// private const val DEFAULT_STATE_INDEX = 1
private const val AUDIOSTATE_SUBSRIPTION_INDEX = 1
private const val PLAYERID_SUBSCRIPTION_INDEX = 2