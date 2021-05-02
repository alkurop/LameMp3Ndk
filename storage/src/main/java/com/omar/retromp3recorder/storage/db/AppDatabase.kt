package com.omar.retromp3recorder.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase

interface DatabaseI {
    fun userDao(): FileDbEntityDao
}

@Database(entities = [FileDbEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase(), DatabaseI {
}

