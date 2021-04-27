package com.omar.retromp3recorder.utils

import java.io.File

interface FileEmptyChecker {
    fun isFileEmpty(filePath: String): Boolean
}

class FileEmptyCheckerImpl : FileEmptyChecker {
    override fun isFileEmpty(filePath: String): Boolean {
        val file = File(filePath)
        return (file.exists() && file.length() > 0).not()
    }
}