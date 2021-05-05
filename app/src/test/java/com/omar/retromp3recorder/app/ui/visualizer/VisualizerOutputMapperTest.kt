package com.omar.retromp3recorder.app.ui.visualizer

import com.omar.retromp3recorder.bl.audio.AudioState
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import org.junit.Before
import org.junit.Test

class VisualizerOutputMapperTest {
    private val outputPublisher: Subject<VisualizerView.Output> = PublishSubject.create()
    private lateinit var test: TestObserver<VisualizerView.State>

    @Before
    fun setUp() {
        test = outputPublisher.compose(VisualizerOutputMapper.mapOutputToState()).test()
    }

    @Test
    fun `map audio state change result`() {
        val stateChangedResult = VisualizerView.Output.AudioStateChanged(AudioState.Playing)
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

    @Test
    fun `map player id changed result`() {
        val playerId = 28
        val playerIdResult = VisualizerView.Output.PlayerIdOutput(playerId)
        //When
        outputPublisher.onNext(playerIdResult)
        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (_, playerId1) -> playerId1 == playerId }
    }
}
