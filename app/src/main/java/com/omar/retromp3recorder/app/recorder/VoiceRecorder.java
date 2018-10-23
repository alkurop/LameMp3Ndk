package com.omar.retromp3recorder.app.recorder;

import io.reactivex.Observable;

public interface VoiceRecorder {

    Observable<VoiceRecorder.Event> observeEvents();

    void record(RecorderProps props);

    void stopRecord();

    enum SampleRate {
        _44100(44100),
        _22050(22050),
        _11025(11025),
        _8000(8000);

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
        final String filepath;
        final BitRate bitRate;
        final SampleRate sampleRate;

        public RecorderProps(String filepath, BitRate bitRate, SampleRate sampleRate) {
            this.filepath = filepath;
            this.bitRate = bitRate;
            this.sampleRate = sampleRate;
        }
    }
}
