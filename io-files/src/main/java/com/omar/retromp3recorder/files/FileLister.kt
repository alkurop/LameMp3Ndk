package com.omar.retromp3recorder.files

interface FileLister {
    fun listFiles(dirPath: String): List<String>
}