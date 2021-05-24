package com.omar.retromp3recorder.storage.db

import androidx.room.*
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.repo.WaveForm

@Entity
data class FileDbEntity(
    @PrimaryKey val created: Long,
    val lastModified: Long,
    val filepath: String,
    @Embedded
    val waveform: WaveformDbEntity?
)

@Entity
data class WaveformDbEntity(
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val waveform: ByteArray
)

@Dao
interface FileDbEntityDao {
    @Query("SELECT * FROM FileDbEntity")
    fun getAll(): List<FileDbEntity>

    @Query("SELECT * from FileDbEntity WHERE filepath = :filepath")
    fun getByFilepath(filepath: String): List<FileDbEntity>

    @Insert
    fun insert(items: List<FileDbEntity>)

    @Update
    fun updateItem(item: FileDbEntity)

    @Delete
    fun delete(items: List<FileDbEntity>)

    @Query("DELETE from FileDbEntity where filepath=:filePath")
    fun deleteByFilepath(filePath: String)
}

fun FileDbEntity.toFileWrapper(): ExistingFileWrapper =
    ExistingFileWrapper(this.filepath, this.created, this.lastModified)

fun ExistingFileWrapper.toDatabaseEntity(): FileDbEntity = FileDbEntity(
    this.createTimedStamp, this.modifiedTimestamp, this.path, waveform = null
)

fun WaveForm.toDatabaseEntity() = WaveformDbEntity(this.data)
fun WaveformDbEntity.toWaveForm() = WaveForm(this.waveform)

