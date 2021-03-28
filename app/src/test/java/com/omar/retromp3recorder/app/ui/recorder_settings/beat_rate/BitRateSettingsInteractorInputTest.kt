package com.omar.retromp3recorder.app.ui.recorder_settings.beat_rate

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.BitRateRepo
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class BitRateSettingsInteractorInputTest {

    @Inject
    lateinit var interactor: BitRateSettingsInteractor

    @Inject
    lateinit var bitRateRepo: BitRateRepo

    private val inputSubject: Subject<Mp3VoiceRecorder.BitRate> = PublishSubject.create()

    private lateinit var test: TestObserver<Mp3VoiceRecorder.BitRate>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = inputSubject.compose(interactor.processIO()).test()
    }

    @Test
    fun `bitrate change input leads to usecase execution`() {
        val bitRate = Mp3VoiceRecorder.BitRate._160

        //When
        inputSubject.onNext(
                bitRate
        )

        //Then
        bitRateRepo.observe().test().assertValue(bitRate)
    }
}