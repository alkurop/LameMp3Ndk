package com.omar.retromp3recorder.app.share

import com.github.alkurop.stringerbell.Stringer.Companion.ofString
import com.omar.retromp3recorder.app.di.AppComponent
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent
import com.omar.retromp3recorder.app.di.MockModule
import com.omar.retromp3recorder.app.share.Sharer.Event.SharingError
import com.omar.retromp3recorder.app.share.Sharer.Event.SharingOk
import io.reactivex.Scheduler
import io.reactivex.subjects.Subject
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject
import javax.inject.Named

class LoggingSharingModuleTest {
    @Inject
    lateinit var logRepo: LogRepo

    @Inject
    lateinit var scheduler: Scheduler

    @Inject
    @Named(AppComponent.DECORATOR_A)
    lateinit var sharingModule: Sharer
    lateinit var spy: Sharer
    lateinit var loggingSharingModule: LoggingSharingModule

    @Inject
    @Named(MockModule.SHARING_SUBJECT)
    lateinit var eventSubject: Subject<Sharer.Event>

    @Before
    fun setUp() {
        DaggerTestAppComponent.create().inject(this)
        spy = Mockito.spy(sharingModule)
        loggingSharingModule = LoggingSharingModule(spy, logRepo, scheduler)
    }

    @Test
    fun test_DecorateShare() {
        loggingSharingModule.share().test()

        //Then
        Mockito.verify(spy).share()
    }

    @Test
    fun test_PostOKEvents() {
        eventSubject.onNext(SharingOk(ofString("test")))

        //Then
        logRepo.observe().test().assertValue { event: LogRepo.Event ->
            ofString("test") == (event as LogRepo.Event.Message).message
        }
    }

    @Test
    fun test_PostErorEvents() {
        eventSubject.onNext(SharingError(ofString("test")))

        //Then
        logRepo.observe().test().assertValue { event: LogRepo.Event ->
            ofString("test") == (event as LogRepo.Event.Error).error
        }
    }
}