package com.omar.retromp3recorder.utils

import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.toFileWrapper
import java.io.File
import javax.inject.Inject

interface FileLister {
    fun listFiles(dirPathList: List<String>): List<ExistingFileWrapper>
    fun discoverFile(path: String): ExistingFileWrapper
}

class FileListerImpl @Inject constructor() : FileLister {
    override fun listFiles(dirPathList: List<String>): List<ExistingFileWrapper> {
        return dirPathList.map { listFiles(it) }.flatten()
    }

    override fun discoverFile(path: String): ExistingFileWrapper {
        val file = File(path)
        return file.toFileWrapper()
    }

    private fun listFiles(dirPath: String): List<ExistingFileWrapper> {
        val file = File(dirPath)
        return file.listFiles()?.map { it.toFileWrapper() }
            ?: emptyList()
    }
}
