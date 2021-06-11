package com.omar.retromp3recorder.utils

import java.io.File

interface DirCreator {
    fun createDirIfNotExists(filepath: String)
}

class DirCreatorImpl() : DirCreator {
    override fun createDirIfNotExists(filepath: String) {
        val file = File(filepath)
        file.mkdirs()
    }
}