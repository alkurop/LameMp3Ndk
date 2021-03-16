package com.omar.retromp3recorder.app.ui.main

import com.github.alkurop.stringerbell.Stringer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.modules.recording.Mp3VoiceRecorder.BitRate
import com.omar.retromp3recorder.app.modules.recording.Mp3VoiceRecorder.SampleRate
import com.omar.retromp3recorder.app.modules.share.Sharer
import com.omar.retromp3recorder.app.state.*
import com.omar.retromp3recorder.app.usecases.CheckPermissionsUC
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
    fun `bitrate change action leads to usecase execution`() {
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
    fun `sample rate change action leads to usecase execution`() {
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
    fun `sharing action leads to usecase exection`() {
        //When
        actionSubject.onNext(MainView.Action.Share)

        //Then
       sharingModule.observeEvents().test().assertValue(
           Sharer.Event.SharingOk(Stringer.ofString("test"))
       )
    }

    @Test
    fun `play action leads to audioplayer playing`() {

        //When
        actionSubject.onNext(MainView.Action.Play)

        //then
        stateRepo.observe().test().assertValue(AudioState.Playing)
    }

    @Test
    fun `stop action leads to state stopped`() {

        //When
        actionSubject.onNext(MainView.Action.Stop)

        //then
        stateRepo.observe().test().assertValue(AudioState.Idle)
    }

    @Test
    fun `start record action leads to state recording`() {

        //When
        actionSubject.onNext(MainView.Action.Record)

        //then
        stateRepo.observe().test().assertValue(AudioState.Recording)
    }
}