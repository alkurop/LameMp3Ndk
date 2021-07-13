package com.omar.retromp3recorder.storage.db

import androidx.room.*
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.dto.Wavetable

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
    val waveform: ByteArray,
    val stepMillis: Int?
)

@Dao
interface FileDbEntityDao {
    @Query("SELECT * FROM FileDbEntity")
    fun getAll(): List<FileDbEntity>

    @Query("SELECT * from FileDbEntity WHERE filepath = :filepath")
    fun getByFilepath(filepath: String): List<FileDbEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(items: List<FileDbEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(item: FileDbEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(items: List<FileDbEntity>)

    @Delete
    fun delete(items: List<FileDbEntity>)

    @Query("DELETE from FileDbEntity where filepath=:filePath")
    fun deleteByFilepath(filePath: String)
}

fun FileDbEntity.toFileWrapper(): ExistingFileWrapper =
    ExistingFileWrapper(
        this.filepath,
        this.created,
        this.lastModified,
        this.waveform?.toWavetable()
    )

fun ExistingFileWrapper.toDatabaseEntity(): FileDbEntity = FileDbEntity(
    this.createTimedStamp,
    this.modifiedTimestamp,
    this.path,
    waveform = this.wavetable?.toDatabaseEntity()
)

fun Wavetable.toDatabaseEntity() = WaveformDbEntity(this.data, stepMillis)
fun WaveformDbEntity.toWavetable() =
    Wavetable(this.waveform, stepMillis ?: 100 /*old format, may still be present in old versions*/)

