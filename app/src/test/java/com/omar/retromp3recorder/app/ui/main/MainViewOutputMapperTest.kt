package com.omar.retromp3recorder.app.ui.main

import com.omar.retromp3recorder.app.ui.main.MainView.State
import com.omar.retromp3recorder.app.ui.main.MainViewOutputMapper.mapOutputToState
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
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
            ) { (bitRate) -> bitRate == Mp3VoiceRecorder.BitRate._128 }
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
            .assertValueAt(1)
            { (_, _, sampleRate) -> sampleRate == Mp3VoiceRecorder.SampleRate._22050 }
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
            .assertValueAt(1)
            { (_, requestForPermissions) -> requestForPermissions.ghost === permissionsToRequest }
    }


}