package com.omar.retromp3recorder.app.share;

import com.omar.retromp3recorder.app.logger.LogRepo;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.omar.retromp3recorder.app.utils.VarargHelper.createLinkedList;

public class LogginSharingModule implements SharingModule {

    private final SharingModule sharingModule;


    public LogginSharingModule(SharingModule sharingModule, LogRepo logRepo, Scheduler scheduler) {
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
