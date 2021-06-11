package com.omar.retromp3recorder.utils

import android.content.Context
import android.os.Environment

interface FilePathGenerator {
    fun generateFilePath(): String
    val fileDirs: List<String>
}

class FilePathGeneratorImpl(
    private val context: Context
) : FilePathGenerator {
    override fun generateFilePath(): String {
        return fileDirs[0]
    }

    override val fileDirs: List<String>
        get() = listOf(
            "${Environment.getExternalStorageDirectory()}/RetroMp3Recorder",
            context.externalCacheDir.toString()
        )
}
