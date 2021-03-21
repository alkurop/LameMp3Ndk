package com.omar.retromp3recorder.app.ui.main

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.ui.main.MainView.State
import com.omar.retromp3recorder.app.ui.main.MainViewResultMapper.map
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.AudioState
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test

class MainViewOutputMapperTest {
    private val outputPublisher: Subject<MainView.Output> = PublishSubject.create()
    private lateinit var test: TestObserver<State>

    @Before
    fun setUp() {
        test = outputPublisher.compose(map()).test()
    }

    @Test
    fun `mapper starts with default view state`() {
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(1)
    }

    @Test
    fun `map log repo message event`() {
        val messageLogResult = MainView.Output.MessageLogOutput(Stringer.ofString("test"))

        //When
        outputPublisher.onNext(messageLogResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (_, _, _, message) ->
                Stringer.ofString("test") == message.ghost
            }
    }

    @Test
    fun `map log repo error event`() {
        val errorLogResult = MainView.Output.ErrorLogOutput(Stringer.ofString("test"))

        //When
        outputPublisher.onNext(errorLogResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (_, _, error) ->
                Stringer.ofString("test") == error.ghost
            }
    }

    @Test
    fun `map bitrate change result`() {
        val bitrateChangedResult =
            MainView.Output.BitrateChangedOutput(Mp3VoiceRecorder.BitRate._128)
        //When
        outputPublisher.onNext(bitrateChangedResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (_, bitRate) -> bitRate == Mp3VoiceRecorder.BitRate._128 }
    }

    @Test
    fun `map sample rate change result`() {
        val bitrateChangedResult =
            MainView.Output.SampleRateChangeOutput(Mp3VoiceRecorder.SampleRate._22050)

        //When
        outputPublisher.onNext(bitrateChangedResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (_, _, _, _, _, _, sampleRate) -> sampleRate == Mp3VoiceRecorder.SampleRate._22050 }
    }

    @Test
    fun `map audio state change result`() {
        val stateChangedResult = MainView.Output.StateChangedOutput(AudioState.Playing)

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
    fun `map request permission result`() {
        val permissionsToRequest = setOf("test")
        val requestPermissionsResult =
            MainView.Output.RequestPermissionsOutput(permissionsToRequest)

        //When
        outputPublisher.onNext(requestPermissionsResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (_, _, _, _, _, requestForPermissions) -> requestForPermissions.ghost === permissionsToRequest }
    }

    @Test
    fun `map player id changed result`() {
        val playerId = 28
        val playerIdResult = MainView.Output.PlayerIdOutput(playerId)

        //When
        outputPublisher.onNext(playerIdResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (_, _, _, _, playerId1) -> playerId1.ghost == playerId }
    }
}