package com.omar.retromp3recorder.utils

interface FileEmptyChecker {
    fun isFileEmpty(path: String?): Boolean
}