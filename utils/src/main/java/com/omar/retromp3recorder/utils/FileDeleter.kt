package com.omar.retromp3recorder.utils

import java.io.File

interface FileDeleter {
    fun deleteFile(filePath: String)
}

class FileDeleterImpl : FileDeleter {
    override fun deleteFile(filePath: String) {
        val file = File(filePath)
        if (file.exists())
            file.delete()
    }
}