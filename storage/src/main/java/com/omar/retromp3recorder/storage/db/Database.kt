package com.omar.retromp3recorder.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FileDbEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun userDao(): FileDbEntityDao
}