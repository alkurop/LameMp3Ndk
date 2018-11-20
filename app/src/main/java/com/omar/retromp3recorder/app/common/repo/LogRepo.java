package com.omar.retromp3recorder.app.common.repo;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.ReplaySubject;
import io.reactivex.subjects.Subject;

@Singleton
public class LogRepo {

    private final Subject<Event> events = ReplaySubject.create();

    @Inject
    public LogRepo(){}

    public Observable<Event> observe() {
        return events;
    }

    public void newValue(Event event) {
        events.onNext(event);
    }

    //region models
    public interface Event { }

    public static final class Message implements LogRepo.Event {
        public final String message;

        public Message(String message) {
            this.message = message;
        }
    }

    public static final class Error implements LogRepo.Event {
        public final String error;

        public Error(String error) {
            this.error = error;
        }
    }
    //endregion
}
