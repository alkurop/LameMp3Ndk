package com.omar.retromp3recorder.dto

data class FileWrapper(
    val path: String,
    val editTimestamp: Long
)

fun String.toTestFileWrapper(): FileWrapper = FileWrapper(this, 0L)