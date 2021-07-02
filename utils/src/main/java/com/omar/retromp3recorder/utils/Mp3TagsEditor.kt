package com.omar.retromp3recorder.utils

import android.content.Context
import android.os.Build
import com.mpatric.mp3agic.ID3v1Tag
import com.mpatric.mp3agic.Mp3File
import com.omar.retromp3recorder.dto.RecordingTags
import timber.log.Timber
import java.io.File

interface Mp3TagsEditor {
    fun setTags(filepath: String, tags: RecordingTags)
    fun getTags(filepath: String): RecordingTags
    fun getFilenameFromPath(filePath: String): String
}

class Mp3TagsEditorImpl(
    private val context: Context,
    private val recordingTagsDefaultsProvider: RecordingTagsDefaultProvider,
    private val fileEmptyChecker: FileEmptyChecker
) : Mp3TagsEditor {
    override fun setTags(filepath: String, tags: RecordingTags) {
        if (isTagsWork().not()) return
        val mp3File = Mp3File(filepath)
        mp3File.id3v1Tag = ID3v1Tag().apply {
            year = tags.year
            artist = tags.artist
            title = tags.title
        }
        val temp = "${context.cacheDir}/temp.mp3"
        mp3File.save(temp)
        File(temp).copyTo(File(filepath), overwrite = true)
        File(temp).delete()
        val newFile = Mp3File(filepath)
        val id3v1Tag = newFile.id3v1Tag
        Timber.d("Tag $id3v1Tag")
    }

    override fun getFilenameFromPath(filePath: String): String {
        return filePath.split("/").last().split("-").last().split(".").first()
    }

    override fun getTags(filepath: String): RecordingTags {
        val defaults = recordingTagsDefaultsProvider.provideDefaults()
        val titleFromFileName = getFilenameFromPath(filepath)
        return if (isTagsWork() && fileEmptyChecker.isFileEmpty(filepath).not())
            Mp3File(filepath).id3v1Tag.run {
                RecordingTags(
                    year = year ?: defaults.year,
                    artist = artist ?: defaults.artist,
                    title = title ?: titleFromFileName
                )
            } else defaults.copy(title = titleFromFileName)
    }
}

private const val VERSION_TAGS_WORK = Build.VERSION_CODES.O_MR1
private fun isTagsWork() = Build.VERSION.SDK_INT >= VERSION_TAGS_WORK