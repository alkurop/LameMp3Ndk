package com.omar.retromp3recorder.bl.files

import android.media.MediaMetadataRetriever
import com.omar.retromp3recorder.audioplayer.toSeekbarTime
import com.omar.retromp3recorder.dto.Wavetable
import javax.inject.Inject

class EmptyWavetableGenerator @Inject constructor() {
    fun generateWavetable(filepath: String): Wavetable {
        val metaRetriever = MediaMetadataRetriever()
        metaRetriever.setDataSource(filepath)
        val duration =
            metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!!.toLong()
        val data = (0 until duration.toSeekbarTime()).map { 0 }.map { it.toByte() }.toByteArray()
        return Wavetable(data)
    }
}