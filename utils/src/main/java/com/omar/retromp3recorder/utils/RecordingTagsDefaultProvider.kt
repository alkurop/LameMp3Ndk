package com.omar.retromp3recorder.utils

import com.omar.retromp3recorder.dto.RecordingTags
import java.util.*
import javax.inject.Inject

class RecordingTagsDefaultProvider @Inject constructor() {
    fun provideDefaults(): RecordingTags {
        val year = Calendar.getInstance().run {
            timeInMillis = System.currentTimeMillis()
            get(Calendar.YEAR)
        }
        return RecordingTags(
            title = "",
            artist = "RetroMp3Recorder",
            album = "RetroMp3Recorder",
            year = year.toString()
        )
    }
}