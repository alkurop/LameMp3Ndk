package com.omar.retromp3recorder.utils

import java.util.*

private const val PROGRESS_CONVERSION_RATE = 100
typealias SeekbarTime = Int
typealias PlayerTime = Long

fun SeekbarTime.toPlayerTime(): PlayerTime = this.toLong() * PROGRESS_CONVERSION_RATE
fun PlayerTime.toSeekbarTime(): SeekbarTime = (this / PROGRESS_CONVERSION_RATE).toInt()

fun PlayerTime.toDisplay(): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this
    val hours = calendar.get(Calendar.HOUR) - 1
    val minutes = calendar.get(Calendar.MINUTE)
    val seconds = calendar.get(Calendar.SECOND)
    val millis = calendar.get(Calendar.MILLISECOND) / 100
    val hasHours = hours > 0
    val hasMinutes = hasHours || minutes > 0
    val hoursString = hours.toFormat(false)
    val hoursSeparator = if (hasHours) ":" else ""
    val minutesString = minutes.toFormat(hasHours)
    val minutesSeparator = if (hasMinutes) ":" else ""
    val secondsString = seconds.toFormat(hasMinutes)
    val millisString = ".$millis"

    return hoursString + hoursSeparator + minutesString + minutesSeparator + secondsString + millisString
}

private fun Int.toFormat(showFullTime: Boolean): String =
    if (showFullTime) String.format(TIME_FORMAT, this) else if (this > 0) "$this" else ""

private const val TIME_FORMAT = "%02d"
