package com.omar.retromp3recorder.utils

import com.omar.retromp3recorder.dto.FileWrapper
import com.omar.retromp3recorder.dto.toFileWrapper
import java.io.File

interface FileLister {
    fun listFiles(dirPath: String): List<FileWrapper>
}

class FileListerImpl : FileLister {
    override fun listFiles(dirPath: String): List<FileWrapper> {
        val file = File(dirPath)
        return file.listFiles()?.map { it.toFileWrapper() }
            ?.sortedBy { it.editTimestamp }
            ?: emptyList()
    }
}