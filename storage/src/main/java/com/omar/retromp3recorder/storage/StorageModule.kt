package com.omar.retromp3recorder.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.omar.retromp3recorder.storage.db.Database
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
    fun provideDatabase(context: Context): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java, "database-name"
        ).build()
    }
}