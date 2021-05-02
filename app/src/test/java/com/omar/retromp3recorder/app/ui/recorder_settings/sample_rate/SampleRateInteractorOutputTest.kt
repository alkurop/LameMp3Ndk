package com.omar.retromp3recorder.app.ui.recorder_settings.sample_rate

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.SampleRateRepo
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class SampleRateInteractorOutputTest {
    @Inject
    lateinit var interactor: SampleRateInteractor

    @Inject
    lateinit var sampleRateRepo: SampleRateRepo

    private lateinit var test: TestObserver<Mp3VoiceRecorder.SampleRate>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = PublishSubject.create<Mp3VoiceRecorder.SampleRate>()
            .compose(interactor.processIO())
            .test()
    }
    @Test
    fun `interactor listens to sample rate repo`() {
        sampleRateRepo.onNext(Mp3VoiceRecorder.SampleRate._8000)

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { sampleRate: Mp3VoiceRecorder.SampleRate ->
            sampleRate == Mp3VoiceRecorder.SampleRate._8000
        }
    }
}

private const val FIRST_EVENT_INDEX = 1
