package com.omar.retromp3recorder.di

import android.content.SharedPreferences
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.omar.retromp3recorder.storage.db.DatabaseI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class TestStorageModule {
    @Singleton
    @Provides
    fun provideSharedPrefs(editor: SharedPreferences.Editor): SharedPreferences {
        return mock<SharedPreferences>().apply {
            whenever(this.edit()) doReturn editor
        }
    }

    @Provides
    fun provideEditor(): SharedPreferences.Editor {
        return mock<SharedPreferences.Editor>().apply {
            whenever(putBoolean(any(), any())) doReturn this
            whenever(putInt(any(), any())) doReturn this
            whenever(putString(any(), any())) doReturn this
        }
    }

    @Provides
    fun provideDatabase(): DatabaseI {
        return mock()
    }
}