package com.app.givebackrx.localdb

import android.content.Context
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface FreqSearchDao {
    @Query("SELECT * FROM freqsearchentity")
    fun getAll(): List<FreqSearchEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insert(fileentity: FreqSearchEntity)

    @Insert(onConflict = REPLACE)
    suspend fun update(fileentity: FreqSearchEntity)

    @Insert
    fun insertAll(vararg fileentity: FreqSearchEntity)

    @Delete
    fun delete(fileentity: FreqSearchEntity)

    @Query("DELETE FROM freqsearchentity")
    suspend fun deleteAll()
}


@Database(entities = arrayOf(FreqSearchEntity::class), version = 2)
abstract class FreqSearchDataBs : RoomDatabase() {
    abstract fun freqSearchDao(): FreqSearchDao

    companion object {
        fun freqSearchDb(context: Context): FreqSearchDataBs {
            return Room.databaseBuilder(
                context.applicationContext,
                FreqSearchDataBs::class.java,
                "freq_searche"
            ).build()
        }

    }
}
