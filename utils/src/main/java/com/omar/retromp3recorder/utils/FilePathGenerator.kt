package com.omar.retromp3recorder.utils

import android.content.Context

interface FilePathGenerator {
    fun generateFilePath(): String
    val fileDir: String
}

class FilePathGeneratorImpl(
    private val context: Context
) : FilePathGenerator {
    override fun generateFilePath(): String {
        return "$fileDir"
    }

    override val fileDir: String
        get() = context.externalCacheDir.toString()
}
