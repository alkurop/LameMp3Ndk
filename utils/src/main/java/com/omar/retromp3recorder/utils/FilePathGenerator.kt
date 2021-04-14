package com.omar.retromp3recorder.utils

interface FilePathGenerator {
    fun generateFilePath(): String
    val fileDir: String
}