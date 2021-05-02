package com.omar.retromp3recorder.dto

import java.io.File

data class FileWrapper(
    val path: String,
    val createTimeStamp: Long,
    val editTimestamp: Long
)

fun String.toTestFileWrapper(): FileWrapper =
    FileWrapper(this, 0L, 0L)

fun File.toFileWrapper(): FileWrapper =
    FileWrapper(this.path, this.lastModified(), this.lastModified())
