package com.omar.retromp3recorder.utils

import android.media.MediaMetadataRetriever
import javax.inject.Inject

class AudioDurationRetriever @Inject constructor() {
    fun getAudioDurationForExistingFile(filepath: String): Long {
        return try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(filepath)
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
                ?: 0L
        } catch (e: Exception) {
            0
        }
    }
}