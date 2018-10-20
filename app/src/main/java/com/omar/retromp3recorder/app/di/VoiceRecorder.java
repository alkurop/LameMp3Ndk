package com.omar.retromp3recorder.app.di;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface VoiceRecorder {

    Observable<VoiceRecorder.Event> observeEvents();

    Completable record(String filePath, int sampleRate, int bitRate);

    interface Event {
    }

    class Message implements Event {
        public final String message;

        public Message(String message) {
            this.message = message;
        }
    }

    class Error extends Message {
        public Error(String message) {
            super(message);
        }
    }
}
