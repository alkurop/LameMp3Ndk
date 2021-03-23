package com.omar.retromp3recorder.app.ui.main

import com.github.alkurop.ghostinshell.Shell
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.audioplayer.AudioPlayer
import com.omar.retromp3recorder.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.state.repos.BitRateRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo.ShouldRequestPermissions
import com.omar.retromp3recorder.state.repos.SampleRateRepo
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

class MainViewInteractorRepoTest {
    @Inject
    lateinit var interactor: MainViewInteractor

    @Inject
    lateinit var bitRateRepo: BitRateRepo

    @Named(MockModule.PLAYER_SUBJECT)
    @Inject
    lateinit var audioBus: Subject<AudioPlayer.Event>

    @Inject
    lateinit var requestPermissionsRepo: RequestPermissionsRepo

    @Inject
    lateinit var sampleRateRepo: SampleRateRepo
    private lateinit var test: TestObserver<MainView.Output>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = PublishSubject.create<MainView.Input>()
            .compose(interactor.processIO())
            .test()
    }

    @Test
    fun `interactor listens to bitrate repo`() {
        //When
        bitRateRepo.newValue(Mp3VoiceRecorder.BitRate._128)

        //Then
        test.assertValueAt(FIRST_EVENT_INDEX) { result ->
            (result as MainView.Output.BitrateChangedOutput).bitRate === Mp3VoiceRecorder.BitRate._128
        }
    }

    @Test
    fun `interactor listens to state repo`() {
        //When
        audioBus.onNext(AudioPlayer.Event.Message(Stringer.ofString("test")))

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { output: MainView.Output ->
            val message = (output as MainView.Output.MessageLogOutput).message
            Stringer.ofString("test") == message
        }
    }

    @Test
    fun `interacto listens to player id repo`() {
        //When
        audioBus.onNext(AudioPlayer.Event.PlayerId(38))

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { output: MainView.Output ->
            val playerId: Int = (output as MainView.Output.PlayerIdOutput).playerId
            playerId == 38
        }
    }

    @Test
    fun `interactor listens to request permissions repo`() {
        val shouldRequestPermissions: ShouldRequestPermissions =
            ShouldRequestPermissions.Denied(Shell(setOf("test")))

        //When
        requestPermissionsRepo.newValue(shouldRequestPermissions)

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { output: MainView.Output ->
            val requestPermissionsOutput: MainView.Output.RequestPermissionsOutput =
                output as MainView.Output.RequestPermissionsOutput
            val permissions: Set<String> =
                requestPermissionsOutput.permissionsToRequest
            permissions.size == 1 && permissions.contains("test")
        }
    }

    @Test
    fun `interactor listens to sample rate repo`() {
        sampleRateRepo.newValue(Mp3VoiceRecorder.SampleRate._8000)

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { output: MainView.Output ->
            val stateChangedOutput: MainView.Output.SampleRateChangeOutput =
                output as MainView.Output.SampleRateChangeOutput
            val sampleRate: Mp3VoiceRecorder.SampleRate = stateChangedOutput.sampleRate
            sampleRate == Mp3VoiceRecorder.SampleRate._8000
        }
    }

    companion object {
        private const val FIRST_EVENT_INDEX = 3
    }
}