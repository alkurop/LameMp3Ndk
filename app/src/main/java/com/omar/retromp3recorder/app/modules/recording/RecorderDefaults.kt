package com.omar.retromp3recorder.app.modules.recording

import com.omar.retromp3recorder.app.modules.recording.Mp3VoiceRecorder.BitRate
import com.omar.retromp3recorder.app.modules.recording.Mp3VoiceRecorder.SampleRate

data class RecorderDefaults(
    val bitRate: BitRate,
    val sampleRate: SampleRate,
    val filePath: String
)
