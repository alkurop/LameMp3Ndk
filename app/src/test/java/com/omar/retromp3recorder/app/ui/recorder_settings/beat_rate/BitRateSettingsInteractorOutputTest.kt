package com.omar.retromp3recorder.app.ui.recorder_settings.beat_rate

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.BitRateRepo
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class BitRateSettingsInteractorOutputTest {
    @Inject
    lateinit var interactor: BitRateSettingsInteractor

    @Inject
    lateinit var bitrateRepo: BitRateRepo
    private lateinit var test: TestObserver<Mp3VoiceRecorder.BitRate>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = PublishSubject.create<Mp3VoiceRecorder.BitRate>()
            .compose(interactor.processIO())
            .test()
    }

    @Test
    fun `interactor listens to bitrate repo`() {
        //When
        bitrateRepo.onNext(Mp3VoiceRecorder.BitRate._128)
        //Then
        test.assertValueAt(FIRST_EVENT_INDEX) { bitrate ->
            bitrate === Mp3VoiceRecorder.BitRate._128
        }
    }
}

private const val FIRST_EVENT_INDEX = 1
