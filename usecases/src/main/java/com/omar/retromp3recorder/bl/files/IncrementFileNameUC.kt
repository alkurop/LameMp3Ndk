package com.omar.retromp3recorder.bl.files

import android.content.SharedPreferences
import com.omar.retromp3recorder.storage.SharedPrefsKeys
import io.reactivex.rxjava3.core.Completable
import javax.inject.Inject

class IncrementFileNameUC @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {
    fun execute(): Completable {
        return Completable.fromAction {
            val current = sharedPreferences.getInt(SharedPrefsKeys.FILE_NAME, 1)
            sharedPreferences.edit().putInt(SharedPrefsKeys.FILE_NAME, current + 1).apply()
        }
    }
}