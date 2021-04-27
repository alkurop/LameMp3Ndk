package com.omar.retromp3recorder.utils

import java.io.File

interface FileLister {
    fun listFiles(dirPath: String): List<File>
}

class FileListerImpl : FileLister {
    override fun listFiles(dirPath: String): List<File> {
        val file = File(dirPath)
        return file.listFiles()
            ?.sortedBy { it.lastModified() }
            ?: emptyList()
    }
}