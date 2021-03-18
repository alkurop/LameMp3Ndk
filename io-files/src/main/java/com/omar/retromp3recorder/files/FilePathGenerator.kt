package com.omar.retromp3recorder.files

interface FilePathGenerator {
    fun generateFilePath(): String
    val fileDir: String
}