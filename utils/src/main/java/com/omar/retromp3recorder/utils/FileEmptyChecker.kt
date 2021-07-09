package com.omar.retromp3recorder.utils

import java.io.File
import javax.inject.Inject

interface FileEmptyChecker {
    fun isFileEmpty(filePath: String): Boolean
}

class FileEmptyCheckerImpl @Inject constructor(
    private val audioDurationRetriever: AudioDurationRetriever
) :
    FileEmptyChecker {
    override fun isFileEmpty(filePath: String): Boolean {
        val file = File(filePath)
        val audioDurationForExistingFile =
            audioDurationRetriever.getAudioDurationForExistingFile(filePath)
        return (file.exists()
                && file.length() > 0).not()
                || audioDurationForExistingFile < 10L
    }
}