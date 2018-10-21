package com.omar.retromp3recorder.app.logger;

import com.omar.retromp3recorder.app.di.VoiceRecorder;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class LogRepo {

    private final PublishSubject<Event> events = PublishSubject.create();

    public Observable<Event> observe() {
        return events;
    }

    public void newValue(Event event) {
        events.onNext(event);
    }

    public interface Event {
    }

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
}
