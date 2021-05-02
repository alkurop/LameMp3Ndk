package com.omar.retromp3recorder.app.ui.recorder_settings.sample_rate

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.SampleRateRepo
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SampleRateInteractorInputTest{
    @Inject
    lateinit var interactor: SampleRateInteractor

    @Inject
    lateinit var sampleRateRepo: SampleRateRepo

    private val inputSubject: Subject<Mp3VoiceRecorder.SampleRate> = PublishSubject.create()

    private lateinit var test: TestObserver<Mp3VoiceRecorder.SampleRate>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = inputSubject.compose(interactor.processIO()).test()
    }



    @Test
    fun `sample rate change input leads to usecase execution`() {
        val sampleRate = Mp3VoiceRecorder.SampleRate._11025

        //When
        inputSubject.onNext(
                sampleRate
        )

        //Then
        sampleRateRepo.observe().test().assertValue(sampleRate)
    }

}