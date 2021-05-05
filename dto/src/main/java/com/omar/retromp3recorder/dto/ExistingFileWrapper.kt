package com.omar.retromp3recorder.dto

import java.io.File

sealed class FileWrapper(open val path: String)
data class FutureFileWrapper(
    override val path: String
) : FileWrapper(path)

data class ExistingFileWrapper(
    override val path: String,
    val createTimedStamp: Long,
    val modifiedTimestamp: Long = 0L
) : FileWrapper(path)

fun String.toFutureFileWrapper(): FutureFileWrapper =
    FutureFileWrapper(this)

fun String.toTestExistingFileWrapper(): ExistingFileWrapper =
    ExistingFileWrapper(this, 0L, 0L)

fun File.toFileWrapper(): ExistingFileWrapper =
    ExistingFileWrapper(this.path, this.lastModified(), this.lastModified())
