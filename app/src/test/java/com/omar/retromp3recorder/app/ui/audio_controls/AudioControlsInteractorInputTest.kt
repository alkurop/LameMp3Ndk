package com.omar.retromp3recorder.app.ui.audio_controls

import com.github.alkurop.stringerbell.Stringer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.bl.CheckPermissionsUC
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class AudioControlsInteractorInputTest {
    @Inject
    lateinit var interactor: AudioControlsInteractor

    @Inject
    lateinit var stateRepo: AudioStateRepo

    @Inject
    lateinit var permissionsUC: CheckPermissionsUC

    @Inject
    lateinit var requestPermissionsRepo: RequestPermissionsRepo

    @Inject
    lateinit var sharingModule: Sharer

    private val actionSubject: Subject<AudioControlsView.Input> = PublishSubject.create()
    private lateinit var test: TestObserver<AudioControlsView.Output>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = actionSubject.compose(interactor.processIO()).test()
        whenever(permissionsUC.execute(any()))
            .thenAnswer {
                requestPermissionsRepo.newValue(RequestPermissionsRepo.ShouldRequestPermissions.Granted)
                Completable.complete()
            }
    }

    @Test
    fun `sharing action leads to usecase exection`() {
        //When
        actionSubject.onNext(AudioControlsView.Input.Share)

        //Then
        sharingModule.observeEvents().test().assertValue(
            Sharer.Event.SharingOk(Stringer.ofString("test"))
        )
    }

    @Test
    fun `play action leads to audioplayer playing`() {

        //When
        actionSubject.onNext(AudioControlsView.Input.Play)

        //then
        stateRepo.observe().test().assertValue(AudioState.Playing)
    }

    @Test
    fun `stop action leads to state stopped`() {

        //When
        actionSubject.onNext(AudioControlsView.Input.Stop)

        //then
        stateRepo.observe().test().assertValue(AudioState.Idle(false))
    }

    @Test
    fun `start record action leads to state recording`() {

        //When
        actionSubject.onNext(AudioControlsView.Input.Record)

        //then
        stateRepo.observe().test().assertValue(AudioState.Recording)
    }
}