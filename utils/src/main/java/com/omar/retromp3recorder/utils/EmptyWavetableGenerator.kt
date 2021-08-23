package com.omar.retromp3recorder.utils

import android.media.MediaMetadataRetriever
import com.omar.retromp3recorder.dto.Wavetable
import com.omar.retromp3recorder.utils.Constants.PLAYER_TO_RECORDER_CONVERSION_MILLIS
import javax.inject.Inject

class EmptyWavetableGenerator @Inject constructor(
    private val fileEmptyChecker: FileEmptyChecker
) {
    fun generateWavetable(filepath: String): Wavetable {
        return if (fileEmptyChecker.isFileEmpty(filepath).not()) {
            val metaRetriever = MediaMetadataRetriever()
            metaRetriever.setDataSource(filepath)
            val duration =
                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!
                    .toLong()
            val data =
                (0 until duration.toSeekbarTime()).map { 0 }.map { it.toByte() }.toByteArray()
            Wavetable(data, PLAYER_TO_RECORDER_CONVERSION_MILLIS)
        } else {
            Wavetable(ByteArray(0), PLAYER_TO_RECORDER_CONVERSION_MILLIS)
        }
    }
}