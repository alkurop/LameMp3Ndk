package com.omar.retromp3recorder.app.ui.main

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.state.repos.BitRateRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo
import com.omar.retromp3recorder.state.repos.RequestPermissionsRepo.ShouldRequestPermissions
import com.omar.retromp3recorder.state.repos.SampleRateRepo
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
    fun `interactor listens to request permissions repo`() {
        val shouldRequestPermissions: ShouldRequestPermissions =
            ShouldRequestPermissions.Denied(Shell(setOf("test")))

        //When
        requestPermissionsRepo.onNext(shouldRequestPermissions)

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
}

private const val FIRST_EVENT_INDEX = 0
