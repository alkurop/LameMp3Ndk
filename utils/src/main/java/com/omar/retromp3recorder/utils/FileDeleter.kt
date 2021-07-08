package com.omar.retromp3recorder.utils

import java.io.File
import javax.inject.Inject

interface FileDeleter {
    fun deleteFile(filePath: String)
}

class FileDeleterImpl @Inject constructor() : FileDeleter {
    override fun deleteFile(filePath: String) {
        val file = File(filePath)
        if (file.exists())
            file.delete()
    }
}