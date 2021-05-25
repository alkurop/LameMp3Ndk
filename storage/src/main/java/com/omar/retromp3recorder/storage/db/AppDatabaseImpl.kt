package com.omar.retromp3recorder.storage.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

interface AppDatabase {
    fun fileEntityDao(): FileDbEntityDao
}

@Database(
    entities = [FileDbEntity::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class AppDatabaseImpl : RoomDatabase(), AppDatabase


