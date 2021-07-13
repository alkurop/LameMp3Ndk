package com.omar.retromp3recorder.dto

data class Wavetable(
    val data: ByteArray,
    val stepMillis: Int
)

data class PlayerProgress(
    val progress: Long,
    val duration: Long
)