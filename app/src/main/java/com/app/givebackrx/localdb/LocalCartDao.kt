package com.app.givebackrx.localdb

import android.content.Context
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

@Dao
interface LocalCartDao {
    @Query("SELECT * FROM localcartentity")
    fun getAll(): List<LocalCartEntity>

    @Insert(onConflict = REPLACE)
    suspend fun insert(localcartentity: LocalCartEntity)

    @Insert(onConflict = REPLACE)
    suspend fun update(localcartentity: LocalCartEntity)

    @Query("UPDATE LocalCartEntity SET quantity = :quantity WHERE id = :uid")
    suspend fun update(uid: Long, quantity: String)

    @Insert
    fun insertAll(vararg localcartentity: LocalCartEntity)

    @Delete
    fun delete(localcartentity: LocalCartEntity)

    @Query("DELETE FROM localcartentity")
    suspend fun deleteAll()
}


@Database(entities = arrayOf(LocalCartEntity::class), version = 2)
abstract class LocalCartDataBs : RoomDatabase() {
    abstract fun localCartDao(): LocalCartDao

    companion object {
        fun localCartDb(context: Context): LocalCartDataBs {
            return Room.databaseBuilder(
                context.applicationContext,
                LocalCartDataBs::class.java,
                "localcart"
            ).build()
        }

    }
}
