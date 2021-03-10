package com.omar.retromp3recorder.app.recording.recorder

import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder.BitRate
import com.omar.retromp3recorder.app.recording.recorder.Mp3VoiceRecorder.SampleRate

data class RecorderDefaults(
    val bitRate: BitRate,
    val sampleRate: SampleRate,
    val filePath: String
)
