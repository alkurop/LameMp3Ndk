package com.omar.retromp3recorder.utils

import java.io.File

class FileListerImpl : FileLister {
    override fun listFiles(dirPath: String): List<String> {
        val file = File(dirPath)
        return file.listFiles()?.map { it.absolutePath }?.sorted() ?: emptyList()
    }
}