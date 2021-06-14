package com.omar.retromp3recorder.utils

import android.content.Context
import android.os.Environment
import java.io.File

interface FilePathGenerator {
    fun generateFilePath(): String
    val fileDirs: List<String>
}

class FilePathGeneratorImpl(
    private val context: Context
) : FilePathGenerator {
    override fun generateFilePath(): String {
        return fileDirs.first { File(it).exists() }
    }

    override val fileDirs: List<String>
        get() = listOf(
            "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)}/RetroMp3Recorder",
            context.externalCacheDir.toString(),
            context.cacheDir.toString()
        )
}
