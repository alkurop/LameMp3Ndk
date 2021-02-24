package com.omar.retromp3recorder.app.main

import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo
import com.omar.retromp3recorder.app.common.repo.StateRepo
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.BitRate
import com.omar.retromp3recorder.app.recording.recorder.VoiceRecorder.SampleRate
import com.omar.retromp3recorder.app.recording.repo.BitRateRepo
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo
import com.omar.retromp3recorder.app.recording.usecase.CheckPermissionsUC
import com.omar.retromp3recorder.app.share.ShareUC
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import javax.inject.Inject

class MainViewInteractorActionsTest {
    @Inject
    lateinit var interactor: MainViewInteractor

    @Inject
    lateinit var bitRateRepo: BitRateRepo

    @Inject
    lateinit var sampleRateRepo: SampleRateRepo

    @Inject
    lateinit var shareUC: ShareUC

    @Inject
    lateinit var stateRepo: StateRepo

    @Inject
    lateinit var permissionsUC: CheckPermissionsUC

    @Inject
    lateinit var requestPermissionsRepo: RequestPermissionsRepo

    private val actionSubject: Subject<MainView.Action> = PublishSubject.create()
    private lateinit var test: TestObserver<MainView.Result>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        Mockito.`when`(permissionsUC.execute(ArgumentMatchers.any()))
            .thenAnswer {
                requestPermissionsRepo.newValue(RequestPermissionsRepo.ShouldRequestPermissions.Granted)
                Completable.complete()
            }
        test = actionSubject.compose(interactor.process()).test()
    }

    @Test
    fun test_ChangeBitrate_Action_UC_Executed() {
        val bitRate = BitRate._160

        //When
        actionSubject.onNext(
            MainView.Action.BitRateChange(
                bitRate
            )
        )

        //Then
        bitRateRepo.observe().test().assertValue(bitRate)
    }

    @Test
    fun test_ChangeSampleRate_Action_UC_Executed() {
        val sampleRate = SampleRate._11025

        //When
        actionSubject.onNext(
            MainView.Action.SampleRateChange(
                sampleRate
            )
        )

        //Then
        sampleRateRepo.observe().test().assertValue(sampleRate)
    }

    @Test
    fun test_Share_Action_UC_Executed() {
        //When
        actionSubject.onNext(MainView.Action.Share)

        //Then
        Mockito.verify(shareUC).execute()
    }

    @Test
    fun test_StartPlayback_Action_UC_Executed() {

        //When
        actionSubject.onNext(MainView.Action.Play)

        //then
        stateRepo.observe().test().assertValue(MainView.State.Playing)
    }

    @Test
    fun test_StopPlaybackAndRecord_Action_UC_Executed() {

        //When
        actionSubject.onNext(MainView.Action.Stop)

        //then
        stateRepo.observe().test().assertValue(MainView.State.Idle)
    }

    @Test
    fun test_StartRecord_Action_UC_Executed() {

        //When
        actionSubject.onNext(MainView.Action.Record)

        //then
        stateRepo.observe().test().assertValue(MainView.State.Recording)
    }
}