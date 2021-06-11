package com.omar.retromp3recorder.utils

private const val PROGRESS_CONVERSION_RATE = 100

fun Int.toPlayerTime(): Long = this.toLong() * PROGRESS_CONVERSION_RATE
fun Long.toSeekbarTime(): Int = (this / PROGRESS_CONVERSION_RATE).toInt()