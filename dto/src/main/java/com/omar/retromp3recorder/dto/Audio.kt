package com.omar.retromp3recorder.dto

data class Wavetable(
    val data: ByteArray,
    val stepMillis: Int = 100
)

data class PlayerProgress(
    val progress: Long,
    val duration: Long
)

data class RecorderProgress(
    val progress: Long,
    val wavetable: Wavetable
)