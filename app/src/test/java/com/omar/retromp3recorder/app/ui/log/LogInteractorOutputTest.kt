package com.omar.retromp3recorder.app.ui.log

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

class LogInteractorRepoTest {

    @Inject
    lateinit var interactor: LogInteractor

    @Inject
    lateinit var audioPlayer: AudioPlayer

    @Inject
    @Named(MockModule.PLAYER_SUBJECT)
    lateinit var audioBus: Subject<AudioPlayer.Event>

    private lateinit var test: TestObserver<LogView.Output>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = PublishSubject.create<LogView.Input>()
            .compose(interactor.processIO())
            .test()
    }

    @Test
    fun `interactor listens to log repo messages`() {
        val message = Stringer.ofString("hello")
        audioBus.onNext(AudioPlayer.Event.Message(message))

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { output: LogView.Output ->
            output as LogView.Output.MessageLogOutput
            output.message == message
        }
    }

    @Test
    fun `interactor listens to sample log repo errors`() {
        val message = Stringer.ofString("hello")
        audioBus.onNext(AudioPlayer.Event.Error(message))

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { output: LogView.Output ->
            output as LogView.Output.ErrorLogOutput
            output.error == message
        }
    }
}

private const val FIRST_EVENT_INDEX = 0