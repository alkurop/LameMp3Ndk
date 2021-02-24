package com.omar.retromp3recorder.app.main

import com.omar.retromp3recorder.app.common.repo.LogRepo
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo.Denied
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo.ShouldRequestPermissions
import com.omar.retromp3recorder.app.common.repo.StateRepo
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.playback.repo.PlayerIdRepo
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.SampleRate
import com.omar.retromp3recorder.app.recording.repo.BitRateRepo
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo
import com.omar.retromp3recorder.app.utils.VarargHelper
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class MainViewInteractorRepoTest {
    @Inject
    lateinit var interactor: MainViewInteractor

    @Inject
    lateinit var bitRateRepo: BitRateRepo

    @Inject
    lateinit var logRepo: LogRepo

    @Inject
    lateinit var playerIdRepo: PlayerIdRepo

    @Inject
    lateinit var requestPermissionsRepo: RequestPermissionsRepo

    @Inject
    lateinit var stateRepo: StateRepo

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
    fun test_Listening_Default_Config_Values() {
        test.assertValueCount(FIRST_EVENT_INDEX)
    }

    @Test
    fun test_Listening_BitRateRepo() {
        //When
        bitRateRepo.newValue(VoiceRecorder.BitRate._128)

        //Then
        test.assertValueAt(FIRST_EVENT_INDEX) { result ->
            (result as MainView.Result.BitrateChangedResult).bitRate === VoiceRecorder.BitRate._128
        }
    }

    @Test
    fun test_Listening_LogRepo() {
        //When
        logRepo.newValue(LogRepo.Message("test"))

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { result: MainView.Result ->
            val message: String = (result as MainView.Result.MessageLogResult).message
            "test" == message
        }
    }

    @Test
    fun test_Listening_PlayerIdRepo() {
        //When
        playerIdRepo.newValue(38)

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { result: MainView.Result ->
            val playerId: Int = (result as MainView.Result.PlayerIdResult).playerId
            playerId == 38
        }
    }

    @Test
    fun test_Listening_RequestPermissionsRepo() {
        val shouldRequestPermissions: ShouldRequestPermissions =
            Denied(VarargHelper.createHashSet("test"))

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
    fun test_Listening_StateRepo() {
        stateRepo.newValue(MainView.State.Recording)

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { result: MainView.Result ->
            val stateChangedResult: MainView.Result.StateChangedResult = result as MainView.Result.StateChangedResult
            val state: MainView.State =
                stateChangedResult.state
            state === MainView.State.Recording
        }
    }

    @Test
    fun test_Listening_SampleRateRepo() {
        sampleRateRepo.newValue(SampleRate._8000)

        //Then
        test.assertValueAt(
            FIRST_EVENT_INDEX
        ) { result: MainView.Result ->
            val stateChangedResult: MainView.Result.SampleRateChangeResult = result as MainView.Result.SampleRateChangeResult
            val sampleRate: SampleRate = stateChangedResult.sampleRate
            sampleRate == SampleRate._8000
        }
    }

    companion object {
        private const val FIRST_EVENT_INDEX = 3
    }
}