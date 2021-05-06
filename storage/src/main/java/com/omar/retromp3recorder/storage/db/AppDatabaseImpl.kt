package com.omar.retromp3recorder.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase

interface AppDatabase {
    fun fileEntityDao(): FileDbEntityDao
}

@Database(entities = [FileDbEntity::class], version = 1)
abstract class AppDatabaseImpl : RoomDatabase(), AppDatabase {
}

