package com.omar.retromp3recorder.utils

import java.io.File

fun String?.fileIfExistsAndNotEmpty(): File? {
    if (this == null || this.isEmpty()) return null
    val file = File(this)
    return if (file.exists() && file.length() > 0) file
    else null
}