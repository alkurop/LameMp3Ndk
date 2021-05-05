package com.omar.retromp3recorder.app.ui.files.preview

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

class CurrentFileInteractorOutputTest {
    @Inject
    lateinit var interactor: CurrentFileInteractor

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    @Inject
    lateinit var audioPlayer: AudioPlayer

    @Inject
    lateinit var mp3VoiceRecorder: Mp3VoiceRecorder

    @Inject
    @Named(MockModule.RECORDER_SUBJECT)
    lateinit var recorderBus: Subject<Mp3VoiceRecorder.Event>
    private lateinit var test: TestObserver<CurrentFileView.Output>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = PublishSubject.create<CurrentFileView.Input>()
            .compose(interactor.processIO())
            .test()
    }

    @Test
    fun `interactor listens to current file repo`() {
        currentFileRepo.onNext(Optional("test1"))

        test.assertValueAt(0) { value ->
            value == CurrentFileView.Output.CurrentFileOutput(null)
        }
        test.assertValueAt(3) { value ->
            value == CurrentFileView.Output.CurrentFileOutput("test1")
        }
    }
}