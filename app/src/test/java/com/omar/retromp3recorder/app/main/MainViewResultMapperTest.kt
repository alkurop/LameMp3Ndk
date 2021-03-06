package com.omar.retromp3recorder.app.main

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.main.MainView.MainViewModel
import com.omar.retromp3recorder.app.main.MainViewResultMapper.map
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test

class MainViewResultMapperTest {
    private val resultPublisher: Subject<MainView.Result> = PublishSubject.create()
    private lateinit var test: TestObserver<MainViewModel>

    @Before
    fun setUp() {
        test = resultPublisher.compose(map()).test()
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
        val messageLogResult = MainView.Result.MessageLogResult(Stringer.ofString("test"))

        //When
        resultPublisher.onNext(messageLogResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (_, _, message) ->
                Stringer.ofString("test") == message.ghost
            }
    }

    @Test
    fun `map log repo error event`() {
        val errorLogResult = MainView.Result.ErrorLogResult(Stringer.ofString("test"))

        //When
        resultPublisher.onNext(errorLogResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (_, error) ->
                Stringer.ofString("test") == error.ghost
            }
    }

    @Test
    fun `map bitrate change result`() {
        val bitrateChangedResult = MainView.Result.BitrateChangedResult(Mp3VoiceRecorder.BitRate._128)
        //When
        resultPublisher.onNext(bitrateChangedResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (bitRate) -> bitRate == Mp3VoiceRecorder.BitRate._128 }
    }

    @Test
    fun `map sample rate change result`() {
        val bitrateChangedResult =
            MainView.Result.SampleRateChangeResult(Mp3VoiceRecorder.SampleRate._22050)

        //When
        resultPublisher.onNext(bitrateChangedResult)

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
        val stateChangedResult = MainView.Result.StateChangedResult(MainView.State.Playing)

        //When
        resultPublisher.onNext(stateChangedResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (_, _, _, _, _, state) -> state === MainView.State.Playing }
    }

    @Test
    fun `map request permission result`() {
        val permissionsToRequest = setOf("test")
        val requestPermissionsResult =
            MainView.Result.RequestPermissionsResult(permissionsToRequest)

        //When
        resultPublisher.onNext(requestPermissionsResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (_, _, _, _, requestForPermissions) -> requestForPermissions.ghost === permissionsToRequest }
    }

    @Test
    fun `map player id changed result`() {
        val playerId = 28
        val playerIdResult = MainView.Result.PlayerIdResult(playerId)

        //When
        resultPublisher.onNext(playerIdResult)

        //Then
        test.assertNoErrors()
            .assertNotComplete()
            .assertNoErrors()
            .assertValueCount(2)
            .assertValueAt(
                1
            ) { (_, _, _, playerId1) -> playerId1.ghost == playerId }
    }
}