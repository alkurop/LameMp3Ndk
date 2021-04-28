package com.omar.retromp3recorder.dto

fun String.toTestFileWrapper(): FileWrapper = FileWrapper(this, 0L)
