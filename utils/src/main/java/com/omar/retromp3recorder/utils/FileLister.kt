package com.omar.retromp3recorder.utils

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.toFileWrapper
import java.io.File

interface FileLister {
    fun listFiles(dirPathList: List<String>): List<ExistingFileWrapper>
}

class FileListerImpl : FileLister {
    override fun listFiles(dirPathList: List<String>): List<ExistingFileWrapper> {
        return dirPathList.map { listFiles(it) }.flatten()
    }

    private fun listFiles(dirPath: String): List<ExistingFileWrapper> {
        val file = File(dirPath)
        return file.listFiles()?.map { it.toFileWrapper() }
            ?: emptyList()
    }
}
