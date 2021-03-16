package com.omar.retromp3recorder.files_manipulation

interface FilePathGenerator {
    fun generateFilePath(): String
    val fileDir: String
}