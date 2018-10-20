package com.omar.retromp3recorder.app.di;

import io.reactivex.Observable;

public interface VoiceRecorder {

    Observable<VoiceRecorder.Event> observeEvents();

    void record(String filePath, int sampleRate, int bitRate);

    void stopRecord();

    interface Event { }

    final class Message implements Event {
        public final String message;

        public Message(String message) {
            this.message = message;
        }
    }

    final class Error implements Event {
        public final String error;

        public Error(String error) {
            this.error = error;
        }
    }
}
