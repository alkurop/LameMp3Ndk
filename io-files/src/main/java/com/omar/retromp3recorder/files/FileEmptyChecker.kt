package com.omar.retromp3recorder.files

interface FileEmptyChecker {
    fun isFileEmpty(path: String?): Boolean
}