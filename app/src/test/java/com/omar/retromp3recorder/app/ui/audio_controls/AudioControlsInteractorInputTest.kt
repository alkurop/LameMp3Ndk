package com.omar.retromp3recorder.app.ui.audio_controls

import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.state.repos.AudioState
import com.omar.retromp3recorder.state.repos.AudioStateMapper
import com.omar.retromp3recorder.state.repos.CurrentFileRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo
import com.omar.retromp3recorder.utils.Optional
import io.reactivex.rxjava3.observers.TestObserver
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class AudioControlsInteractorInputTest {
    @Inject
    lateinit var interactor: AudioControlsInteractor

    @Inject
    lateinit var stateMapper: AudioStateMapper

    @Inject
    lateinit var requestPermissionsRepo: RequestPermissionsRepo

    @Inject
    lateinit var sharingModule: Sharer

    @Inject
    lateinit var currentFileRepo: CurrentFileRepo

    private val actionSubject: Subject<AudioControlsView.Input> = PublishSubject.create()
    private lateinit var test: TestObserver<AudioControlsView.Output>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        test = actionSubject.compose(interactor.processIO()).test()
        currentFileRepo.onNext(Optional("test"))
    }

    @Test
    fun `sharing action leads to usecase execution`() {
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
        stateMapper.observe().test().assertValue(AudioState.Playing)
    }

    @Test
    fun `stop action leads to state stopped`() {

        //When
        actionSubject.onNext(AudioControlsView.Input.Stop)

        //then
        stateMapper.observe().test().assertValue(AudioState.Idle)
    }

    @Test
    fun `start record action leads to state recording`() {

        //When
        actionSubject.onNext(AudioControlsView.Input.Record)

        //then
        stateMapper.observe().test().assertValue(AudioState.Recording)
    }
}