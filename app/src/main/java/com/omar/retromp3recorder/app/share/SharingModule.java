package com.omar.retromp3recorder.app.share;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface SharingModule {

    Completable share();

    Observable<Event> observeEvents();

    interface Event { }

    final class SharingOk implements Event {
        public final String message;

        SharingOk(String message) {
            this.message = message;
        }
    }

    final class SharingError implements Event {
        public final String error;

        SharingError(String error) {
            this.error = error;
        }
    }
}
