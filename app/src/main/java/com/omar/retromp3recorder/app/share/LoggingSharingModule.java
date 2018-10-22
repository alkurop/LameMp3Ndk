package com.omar.retromp3recorder.app.share;

import com.omar.retromp3recorder.app.repo.LogRepo;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.omar.retromp3recorder.app.di.AppComponent.INTERNAL;
import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

public final class LoggingSharingModule implements SharingModule {

    private final SharingModule sharingModule;

    @Inject
    public LoggingSharingModule(
            @Named(INTERNAL) SharingModule sharingModule,
            LogRepo logRepo,
            Scheduler scheduler
    ) {
        this.sharingModule = sharingModule;
        Observable<SharingModule.Event> share = sharingModule.observeEvents().share();
        Observable<LogRepo.Event> message = share.ofType(SharingModule.SharingOk.class)
                .map(answer -> new LogRepo.Message(answer.message));
        Observable<LogRepo.Event> error = share.ofType(SharingModule.SharingError.class)
                .map(answer -> new LogRepo.Message(answer.error));

        Observable
                .merge(createLinkedList(
                        message, error
                ))
                .flatMapCompletable(event -> Completable.fromAction(() ->
                        logRepo.newValue(event)
                ))
                .subscribeOn(scheduler)
                .subscribe();
    }


    @Override
    public Completable share() {
        return sharingModule.share();
    }

    @Override
    public Observable<Event> observeEvents() {
        return sharingModule.observeEvents();
    }
}