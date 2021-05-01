package com.omar.retromp3recorder.storage.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Entity
data class FileDbEntity(
    @PrimaryKey val created: Long,
    val lastModified: Long,
    val filepath: String
)

@Dao
interface FileDbEntityDao {
    @Query("SELECT * FROM FileDbEntity")
    fun getAll(): Single<List<FileDbEntity>>

    @Query("SELECT * from FileDbEntity WHERE filepath = :filepath")
    fun getByFilepath(filepath: String): Single<FileDbEntity>

    @Insert
    fun insert(items: List<FileDbEntity>): Completable

    @Update
    fun updateItem(item: FileDbEntity): Completable

    @Delete
    fun delete(items: List<FileDbEntity>): Completable
}