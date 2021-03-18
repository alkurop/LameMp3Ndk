package com.omar.retromp3recorder.files

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.omar.retromp3recorder.utils.NotUnitTestable
import java.io.File
import javax.inject.Inject

@NotUnitTestable
class FileUriCreator @Inject constructor(private val context: Context) {
    fun createSharableUri(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName +
                    ".com.omar.retromp3recorder.app.provider",
            file
        )
    }
}
