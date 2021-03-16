package com.omar.retromp3recorder.app.recorder

data class RecorderDefaults(
    val bitRate: Mp3VoiceRecorder.BitRate,
    val sampleRate: Mp3VoiceRecorder.SampleRate,
    val filePath: String
)
