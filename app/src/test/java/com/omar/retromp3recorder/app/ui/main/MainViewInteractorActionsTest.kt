package com.omar.retromp3recorder.app.ui.main

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.BitRateRepo
import com.omar.retromp3recorder.state.repos.SampleRateRepo
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class MainViewInteractorActionsTest {
    @Inject
    lateinit var interactor: MainViewInteractor

    @Inject
    lateinit var bitRateRepo: BitRateRepo

    @Inject
    lateinit var sampleRateRepo: SampleRateRepo

    private val inputSubject: Subject<MainView.Input> = PublishSubject.create()
    private lateinit var test: TestObserver<MainView.Output>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = inputSubject.compose(interactor.processIO()).test()
    }

    @Test
    fun `bitrate change action leads to usecase execution`() {
        val bitRate = Mp3VoiceRecorder.BitRate._160

        //When
        inputSubject.onNext(
            MainView.Input.BitRateChange(
                bitRate
            )
        )

        //Then
        bitRateRepo.observe().test().assertValue(bitRate)
    }

    @Test
    fun `sample rate change action leads to usecase execution`() {
        val sampleRate = Mp3VoiceRecorder.SampleRate._11025

        //When
        inputSubject.onNext(
            MainView.Input.SampleRateChange(
                sampleRate
            )
        )

        //Then
        sampleRateRepo.observe().test().assertValue(sampleRate)
    }


}