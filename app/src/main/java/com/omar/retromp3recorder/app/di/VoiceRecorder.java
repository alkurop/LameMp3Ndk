package com.omar.retromp3recorder.app.di;

import io.reactivex.Observable;

public interface VoiceRecorder {

    Observable<VoiceRecorder.Event> observeEvents();

    void record(RecorderProps props);

    void stopRecord();

    enum SampleRate {
        _44100(44100),
        _22050(22050),
        _11052(11052),
        _8000(8800);

        public final int value;

        SampleRate(int value) {
            this.value = value;
        }
    }

    enum BitRate {
        _320(320), _192(192), _160(160), _128(128);
        public final int value;

        BitRate(int value) {
            this.value = value;
        }
    }

    interface Event {
    }

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

    final class RecorderProps {
        public final String filepath;
        public final BitRate bitRate;
        public final SampleRate sampleRate;

        public RecorderProps(String filepath, BitRate bitRate, SampleRate sampleRate) {
            this.filepath = filepath;
            this.bitRate = bitRate;
            this.sampleRate = sampleRate;
        }
    }
}
