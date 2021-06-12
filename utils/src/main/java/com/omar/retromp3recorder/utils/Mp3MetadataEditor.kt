package com.omar.retromp3recorder.utils

import android.content.Context
import com.mpatric.mp3agic.ID3v1Tag
import com.mpatric.mp3agic.Mp3File
import com.omar.retromp3recorder.dto.RecordingMetadata
import timber.log.Timber
import java.io.File

interface Mp3MetadataEditor {
    fun setMetadata(filepath: String, metadata: RecordingMetadata)
    fun getMetadata(filepath: String): RecordingMetadata
}

class Mp3MetadataEditorImpl(private val context: Context) : Mp3MetadataEditor {
    override fun setMetadata(filepath: String, metadata: RecordingMetadata) {
        val mp3File = Mp3File(filepath)
        mp3File.id3v1Tag = ID3v1Tag().apply {
            year = metadata.year
            artist = metadata.artist
            title = metadata.title
            album = metadata.album
        }
        val temp = "${context.cacheDir}/temp.mp3"
        mp3File.save(temp)


        File(temp).copyTo(File(filepath), overwrite = true)
        File(temp).delete()
        val newFile = Mp3File(filepath)
        val id3v1Tag = newFile.id3v1Tag
        Timber.d("Tag $id3v1Tag")
    }

    override fun getMetadata(filepath: String): RecordingMetadata {
        val metadata = Mp3File(filepath).id3v1Tag.run {
            RecordingMetadata(
                year = year ?: "",
                artist = artist ?: "",
                title = title ?: "",
                album = album ?: ""
            )
        }
        return metadata
    }
}