package com.omar.retromp3recorder.app.share;

import com.omar.retromp3recorder.app.common.repo.LogRepo;
import com.omar.retromp3recorder.app.di.DaggerTestAppComponent;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Scheduler;
import io.reactivex.subjects.Subject;

import static com.omar.retromp3recorder.app.di.AppComponent.DECORATOR_A;
import static com.omar.retromp3recorder.app.di.MockModule.SHARING_SUBJECT;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class LoggingSharingModuleTest {

    @Inject
    LogRepo logRepo;
    @Inject
    Scheduler scheduler;
    @Inject
    @Named(DECORATOR_A)
    SharingModule sharingModule;
    SharingModule spy;
    LoggingSharingModule loggingSharingModule;
    @Inject
    @Named(SHARING_SUBJECT)
    Subject<SharingModule.Event> eventSubject;

    @Before
    public void setUp() {
        DaggerTestAppComponent.create().inject(this);
        spy = spy(sharingModule);
        loggingSharingModule = new LoggingSharingModule(spy, logRepo, scheduler);
    }

    @Test
    public void test_DecorateShare() {
        loggingSharingModule.share().test();

        //Then
        verify(spy).share();
    }

    @Test
    public void test_PostOKEvents() {
        eventSubject.onNext(new SharingModule.Event.SharingOk("test"));

        //Then
        logRepo.observe().test().assertValue(event ->
                "test".equals(((LogRepo.Event.Message) event).getMessage())
        );
    }

    @Test
    public void test_PostErorEvents() {
        eventSubject.onNext(new SharingModule.Event.SharingError("test"));

        //Then
        logRepo.observe().test().assertValue(event ->
                "test".equals(((LogRepo.Event.Error) event).getError())
        );
    }
}