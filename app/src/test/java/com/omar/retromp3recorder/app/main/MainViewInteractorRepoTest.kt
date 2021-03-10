package com.omar.retromp3recorder.app.main

import com.github.alkurop.ghostinshell.Shell
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.common.repo.LogRepo
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo.ShouldRequestPermissions
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.app.playback.player.AudioPlayer
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder.SampleRate
import com.omar.retromp3recorder.app.recording.repo.BitRateRepo
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo
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

    @Inject
    lateinit var logRepo: LogRepo

    @Named(MockModule.PLAYER_SUBJECT)
    @Inject lateinit var audioBus: Subject<AudioPlayer.Event>

    @Inject
    lateinit var requestPermissionsRepo: RequestPermissionsRepo

    @Inject
    lateinit var voiceRecorder: Mp3VoiceRecorder

    @Inject
    lateinit var sampleRateRepo: SampleRateRepo
    private lateinit var test: TestObserver<MainView.Result>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = PublishSubject.create<MainView.Action>()
            .compose(interactor.process())
            .test()
    }

    @Test
    fun `interactor listens to bitrate repo`() {
        //When
        bitRateRepo.newValue(Mp3VoiceRecorder.BitRate._128)

        //Then
        test.assertValueAt(FIRST_EVENT_INDEX) { result ->
            (result as MainView.Result.BitrateChangedResult).bitRate === Mp3VoiceRecorder.BitRate._128
        }
    }

    @Test
    fun `interactor listens to state repo`() {
        //When
        audioBus.onNext(AudioPlayer.Event.Message(Stringer.ofString("test")))

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { result: MainView.Result ->
            val message  = (result as MainView.Result.MessageLogResult).message
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
        ) { result: MainView.Result ->
            val playerId: Int = (result as MainView.Result.PlayerIdResult).playerId
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
        ) { result: MainView.Result ->
            val requestPermissionsResult: MainView.Result.RequestPermissionsResult =
                result as MainView.Result.RequestPermissionsResult
            val permissions: Set<String> =
                requestPermissionsResult.permissionsToRequest
            permissions.size == 1 && permissions.contains("test")
        }
    }

    @Test
    fun `interactor listens to sample rate repo`() {
        sampleRateRepo.newValue(SampleRate._8000)

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { result: MainView.Result ->
            val stateChangedResult: MainView.Result.SampleRateChangeResult =
                result as MainView.Result.SampleRateChangeResult
            val sampleRate: SampleRate = stateChangedResult.sampleRate
            sampleRate == SampleRate._8000
        }
    }

    companion object {
        private const val FIRST_EVENT_INDEX = 3
    }
}