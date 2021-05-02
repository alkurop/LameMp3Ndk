package com.omar.retromp3recorder.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.DatabaseI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {
    @Provides
    fun provideSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences("app", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideDatabase(context: Context): DatabaseI {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
    }
}