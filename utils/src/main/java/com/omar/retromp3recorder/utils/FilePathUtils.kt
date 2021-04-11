package com.omar.retromp3recorder.utils

import java.io.File

fun String.filepathToFileName(): String {
    return File(this).name
}