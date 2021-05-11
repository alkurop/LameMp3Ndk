package com.omar.retromp3recorder.bl.files

import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import com.omar.retromp3recorder.utils.FilePathGenerator
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Get file path, and then create filename from incremented shared pref
 */
class GetNewFileNameUC @Inject constructor(
    private val filePathGenerator: FilePathGenerator,
    private val sharedPreferences: SharedPreferences
) {
    fun execute(): Single<String> {
        return Single.fromCallable {
            "${filePathGenerator.generateFilePath()}/${
                sharedPreferences.getInt(
                    SharedPrefsKeys.FILE_NAME,
                    1
                )
            }.mp3"
        }
    }
}