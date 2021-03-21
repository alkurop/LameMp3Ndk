package com.omar.retromp3recorder.files

import android.content.Context
import com.omar.retromp3recorder.utils.NotUnitTestable

@NotUnitTestable
class FilePathGeneratorImpl(
    private val context: Context
) : FilePathGenerator {
    override fun generateFilePath(): String {
        val fileName = VOICE_RECORD + "_" + System.currentTimeMillis() + MP3_EXTENSION
        return "$fileDir/$fileName"
    }

    override val fileDir: String
        get() = context.externalCacheDir.toString()
}

private const val MP3_EXTENSION = ".mp3"
private const val VOICE_RECORD = "voice_record"