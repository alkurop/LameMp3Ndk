package com.omar.retromp3recorder.app.recorder;

import android.media.AudioFormat;

import io.reactivex.Observable;

public interface VoiceRecorder {

    Observable<VoiceRecorder.Event> observeEvents();

    void record(RecorderProps props);

    void stopRecord();

    boolean isRecording();

    //region settings

    enum SampleRate {
        _44100(44100), _22050(22050), _11025(11025), _8000(8000);

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

    short[] AUDIO_FORMAT_PRESETS = new short[]{
            AudioFormat.ENCODING_PCM_8BIT,
            AudioFormat.ENCODING_PCM_16BIT
    };

    int[] QUALITY_PRESETS = new int[]{
            2,
            5,
            7
    };  // the lower the better

    short[] CHANNEL_PRESETS = new short[]{
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.CHANNEL_IN_STEREO
    };

    //endregion

    //region events
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
    //endregion
}
