package com.omar.retromp3recorder.app.recording.recorder;

public class RecorderDefaults {
    public final VoiceRecorder.BitRate bitRate;
    public final VoiceRecorder.SampleRate sampleRate;
    public final String filePath;

    public RecorderDefaults(VoiceRecorder.BitRate bitRate, VoiceRecorder.SampleRate sampleRate, String filePath) {
        this.bitRate = bitRate;
        this.sampleRate = sampleRate;
        this.filePath = filePath;
    }
}
