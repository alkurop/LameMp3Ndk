package com.omar.retromp3recorder.app.main

import com.github.alkurop.stringerbell.Stringer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.app.common.repo.AudioState
import com.omar.retromp3recorder.app.common.repo.AudioStateRepo
import com.omar.retromp3recorder.app.common.repo.RequestPermissionsRepo
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder.BitRate
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder.SampleRate
import com.omar.retromp3recorder.app.recording.repo.BitRateRepo
import com.omar.retromp3recorder.app.recording.repo.SampleRateRepo
import com.omar.retromp3recorder.app.recording.usecase.CheckPermissionsUC
import com.omar.retromp3recorder.app.share.Sharer
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class MainViewInteractorActionsTest {
    @Inject
    lateinit var interactor: MainViewInteractor

    @Inject
    lateinit var bitRateRepo: BitRateRepo

    @Inject
    lateinit var sampleRateRepo: SampleRateRepo

    @Inject
    lateinit var sharingModule: Sharer

    @Inject
    lateinit var permissionsUC: CheckPermissionsUC

    @Inject
    lateinit var requestPermissionsRepo: RequestPermissionsRepo

    @Inject
    lateinit var stateRepo: AudioStateRepo

    private val actionSubject: Subject<MainView.Action> = PublishSubject.create()
    private lateinit var test: TestObserver<MainView.Result>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        whenever(permissionsUC.execute(any()))
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
       sharingModule.observeEvents().test().assertValue(
           Sharer.Event.SharingOk(Stringer.ofString("test"))
       )
    }

    @Test
    fun test_StartPlayback_Action_UC_Executed() {

        //When
        actionSubject.onNext(MainView.Action.Play)

        //then
        stateRepo.observe().test().assertValue(AudioState.Playing)
    }

    @Test
    fun test_StopPlaybackAndRecord_Action_UC_Executed() {

        //When
        actionSubject.onNext(MainView.Action.Stop)

        //then
        stateRepo.observe().test().assertValue(AudioState.Idle)
    }

    @Test
    fun test_StartRecord_Action_UC_Executed() {

        //When
        actionSubject.onNext(MainView.Action.Record)

        //then
        stateRepo.observe().test().assertValue(AudioState.Recording)
    }
}