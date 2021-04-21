package com.omar.retromp3recorder.app.ui.files.preview

import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
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
        test.assertValueAt(2) { value ->
            value == CurrentFileView.Output.CurrentFileOutput("test1")
        }
    }

    @Test
    fun `listens to audio state`() {
        audioPlayer.playerStart("test")

        test.assertValueAt(2) { value ->
            value == CurrentFileView.Output.AudioActive
        }
    }
}