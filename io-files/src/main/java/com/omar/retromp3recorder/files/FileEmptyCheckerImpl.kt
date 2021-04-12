package com.omar.retromp3recorder.files

import java.io.File

class FileEmptyCheckerImpl : FileEmptyChecker {
    override fun isFileEmpty(path: String?): Boolean {
        if (path == null) return true
        val file = File(path)
        return (file.exists() && file.length() > 0).not()
    }
}