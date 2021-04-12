package com.omar.retromp3recorder.utils

interface FileLister {
    fun listFiles(dirPath: String): List<String>
}